/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.io;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.xml.parsers.ParserConfigurationException;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.metamodel.core.AbstractDiagram;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.metamodel.core.CommonElementsFactory;
import org.aero.mtip.metamodel.core.CommonRelationship;
import org.aero.mtip.metamodel.core.CommonRelationshipsFactory;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.ElementData;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.MtipUtils;
import org.xml.sax.SAXException;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.uml.Finder;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Enumeration;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class Importer {
  static Importer instance;

  File[] files;

  Map<String, String> importIdToCameoId = new HashMap<String, String>();
  Map<String, String> parentMap = new HashMap<String, String>();
  Set<String> unsupportedElementIds = new HashSet<String>();
  Set<String> invalidOwners = new HashSet<String>();

  HashMap<String, ElementData> allElementData = new HashMap<String, ElementData>();
  HashMap<String, ElementData> stereotypeElementData = new HashMap<String, ElementData>();
  HashMap<String, ElementData> profileElementData = new HashMap<String, ElementData>();

  List<ElementData> properties = new ArrayList<ElementData>();

  Project project;
  Element primaryLocation;
  CommonElementsFactory cef;
  CommonRelationshipsFactory crf;

  public static Importer createNewImporter(File[] files, Element rootPackage) {
    instance = new Importer(files, rootPackage);
    return instance;
  }

  public static void destroy() {
    instance = null;
  }

  public static Importer getInstance() {
    return instance;
  }

  public static void importFromFiles(File[] files, Element rootPackage) throws ParserConfigurationException, IOException, SAXException {
    Importer importer = createNewImporter(files, rootPackage);
    importer.importModel();
  }

  public Importer(File[] files, Element rootPackage) {
    org.aero.mtip.profiles.Profile.clearAllProfiles();

    this.files = files;
    setPrimaryLocation(rootPackage);

    project = Application.getInstance().getProject();
    cef = new CommonElementsFactory();
    crf = new CommonRelationshipsFactory();

    Logger.createNewImportLogger();
  }

  public void importModel() throws ParserConfigurationException, SAXException, IOException {
    for (File file : files) {
      parseFile(file);
    }

    createModel();
    Logger.logSummary(this);
  }

  public void parseFile(File file) throws ParserConfigurationException, SAXException, IOException {
    Parser parser = Parser.getParserForFile(file, project);
    parser.parse(file);

    allElementData.putAll(parser.getAllElementData());
    stereotypeElementData.putAll(parser.getStereotypeElementData());
    profileElementData.putAll(parser.getProfileElementData());

    properties.addAll(parser.getProperties());
  }


  public void createModel() throws NullPointerException {
    project.getOptions().setAutoNumbering(false);

    buildEntities(profileElementData.keySet());
    buildEntities(allElementData.keySet());

    CameoUtils.createSession(project, "Refresh");
    CameoUtils.closeSession(project);

    project.getOptions().setAutoNumbering(true);
  }

  public void buildEntities(Collection<String> importIds) {
    for (String importId : importIds) {
      if (isImported(importId)) {
        continue;
      }

      buildEntity(importId);
    }
  }

  @CheckForNull
  public Element buildEntity(String importId) {
    ElementData elementData = allElementData.get(importId);

    if (elementData == null) {
      return null;
    }

    String category = elementData.getCategory();

    if (category.isEmpty()) {
      Logger.log(String.format("Element %s with id %s is uncategorized and will not be imported.", elementData.getName(),
          elementData.getImportId()));
      return null;
    }

    if (category.equals(SysmlConstants.ELEMENT)) {
      return buildElement(elementData);
    }

    if (category.equals(SysmlConstants.RELATIONSHIP)) {
      return buildRelationship(elementData);
    }

    if (elementData.getCategory().equals(SysmlConstants.DIAGRAM)) {
      return buildDiagram(elementData);
    }

    return null;
  }

  @CheckForNull
  public Element buildElement(ElementData elementData) {
    if (elementData == null) {
      Logger.log("Missing element data on Importer.buildElement()");
      return null;
    }

    if (isImported(elementData.getImportId())) {
      return getImportedElement(elementData.getImportId());
    }

    Element owner = buildEntity(elementData.getParentImportId());

    if (owner == null) {
      owner = project.getPrimaryModel();
    }

    if (!MtipUtils.isSupportedElement(elementData.getType())) {
      Logger.log(String.format("%s type not supported. Import id %s", elementData.getType(), elementData.getImportId()));
      return null;
    }

    CameoUtils.createSession(project, String.format("Initialize element %s Element", elementData.getType()));
    CommonElement element = cef.createElement(elementData.getType(), elementData.getName(), elementData.getImportId());
    CameoUtils.closeSession(project);

    element.createDependentElements(elementData);

    // Create new session if tagged values or other dependent elements are found, built, and session is
    // closed.
    CameoUtils.createSession(project, String.format("Create %s with dependent Elements", elementData.getType()));
    Element importedElement = element.createElement(project, owner, elementData);

    if (importedElement == null) {
      Logger.log(String.format("Element failed to be created with id %s.", elementData.getImportId()));
      return null;
    }

    if (importedElement.getOwner() == null) {
      Logger.log("Owner failed to be set including any default owners. Element with id " + elementData.getImportId() + " not created.");
      unsupportedElementIds.add(elementData.getImportId());
      ModelHelper.dispose(Collections.singletonList(importedElement));

      return null;
    }

    CameoUtils.closeSession(project);

    trackIds(importedElement, elementData);
    addStereotypes(importedElement, elementData);

    element.addStereotypeTaggedValues(elementData);
    element.createReferencedElements(elementData);

    CameoUtils.createSession(project, String.format("Add referenced elements to %s", elementData.getType()));
    element.addReferences();
    CameoUtils.closeSession(project);

    // Logger.log(String.format("Created element %s of type: %s.", modelElement.getName(),
    // modelElement.getType()));
    return importedElement;
  }

  @CheckForNull
  public Element buildRelationship(ElementData elementData) {
    if (elementData == null) {
      Logger.log("Missing element data on  Importer.buildRelationship().");
      return null;
    }

    if (isImported(elementData)) {
      return getImportedElement(elementData.getImportId());
    }

    Element owner = buildEntity(elementData.getParentImportId());

    if (!MtipUtils.isSupportedRelationship(elementData.getType())) {
      Logger.log(String.format("%s type not supported. Import id %s", elementData.getType(), elementData.getImportId()));
      return null;
    }

    Element client = getImportedRelationshipEnd(elementData.getClientImportId());

    if (client == null) {
      Logger.log(String.format("Client null for relationship %s. Import id %s", elementData.getName(), elementData.getImportId()));
      return null;
    }

    Element supplier = getImportedRelationshipEnd(elementData.getSupplierImportId());

    if (supplier == null) {
      Logger.log(String.format("Supplier null for relationship %s. Import id %s", elementData.getName(), elementData.getImportId()));
      return null;
    }

    CameoUtils.createSession(project, String.format("Creating CommonRelationship of type %s.", elementData.getType()));
    CommonRelationship relationship = crf.createElement(elementData.getType(), elementData.getAttribute("name"), elementData.getImportId());
    CameoUtils.closeSession(project);
    
    if (relationship == null) {
      Logger.log("Error. Element missing in common elements factory.");
      return null;
    }
    
//    if (!ModelHelper.canMoveChildInto(owner, relationship.getElement())) {
//      owner = ModelHelper.findParent(relationship.getElement(), client, supplier, null);
//    }
    
    relationship.createDependentElements(elementData);
    
    CameoUtils.createSession(project, String.format("Creating CommonRelationship of type %s.", elementData.getType()));
    Element importedRelationship = relationship.createElement(project, owner, client, supplier, elementData);
    CameoUtils.closeSession(project);
    
    if (importedRelationship == null) {
      Logger.log(String.format("Relationship failed to be created with id %s.", elementData.getImportId()));
      return null;
    }

    if (importedRelationship.getOwner() == null) {
      CameoUtils.createSession(project, String.format("Disposing of relationship with no owner: %s.", elementData.getType()));

      Logger.log(
          String.format("Disposing of relationship with no owner. Type: %s; id: %s", elementData.getType(), elementData.getImportId()));
      invalidOwners.add(elementData.getImportId());

      if (importedRelationship != null) {
        ModelHelper.dispose(Collections.singletonList(importedRelationship));
      }

      CameoUtils.closeSession(project);
      return null;
    }

    trackIds(importedRelationship, elementData);
    relationship.createReferencedElements(elementData);

    return importedRelationship;
  }

  @CheckForNull
  public Element buildDiagram(ElementData elementData) {
    if (elementData == null) {
      Logger.log("Missing element data on Importer.buildDiagram().");
      return null;
    }

    if (isImported(elementData)) {
      return getImportedElement(elementData.getImportId());
    }

    Element owner = buildEntity(elementData.getParentImportId());

    if (owner == null) {
      owner = project.getPrimaryModel();
    }

    if (!MtipUtils.isSupportedDiagram(elementData.getType())) {
      Logger.log(String.format("%s with id %s type is not supported. ", elementData.getType(), elementData.getImportId()));
      return null;
    }

    CameoUtils.createSession(project, String.format("Create %s Element", elementData.getType()));

    AbstractDiagram diagram = (AbstractDiagram) cef.createElement(elementData.getType(), elementData.getName(), elementData.getImportId());
    Diagram newDiagram = (Diagram) diagram.createElement(project, owner, elementData);

    if (newDiagram == null) {
      Logger.log(String.format("Error creating diagram. Check CameoDiagramConstant for diagram with id: %s", elementData.getImportId()));
      return null;
    }

    DiagramPresentationElement dpe = project.getDiagram(newDiagram);

    if (dpe == null) {
      Logger.log(String.format("Unable to find dpe. Cannot populate digram for element %s", elementData.getImportId()));
      return newDiagram;
    }

    dpe.open();
    trackIds(newDiagram, elementData);

    CameoUtils.closeSession(project);

    // Opens and closes its own session while populating the diagram
    populateDiagram(diagram, newDiagram, elementData);
    return newDiagram;
  }

  public void addStereotypes(Element newElement, ElementData elementData) {
    HashMap<String, String> stereotypes = elementData.getStereotypes();

    for (String stereotype : stereotypes.keySet()) {
      addStereotype(newElement, stereotype, stereotypes.get(stereotype));
      addStereotypeFields(newElement, elementData);
    }
  }

  public void addStereotype(Element newElement, String stereotypeName, String profileName) {
    // Need to implement mapping of all SysML base stereotypes and which internal library they come from
    // (SysML, MD Customization for SysML, UML Standard Profile, etc.)
    Profile umlStandardProfile = StereotypesHelper.getProfile(project, "UML Standard Profile");

    if (stereotypeName.contentEquals("metaclass")) {
      Stereotype stereotypeObj = StereotypesHelper.getStereotype(project, "Metaclass", umlStandardProfile);
      StereotypesHelper.addStereotype(newElement, stereotypeObj);
      return;
    }

    Profile profile = StereotypesHelper.getProfile(project, profileName);

    if (profile == null) {
      return;
    }

    Stereotype stereotype = StereotypesHelper.getStereotype(project, stereotypeName, profile);

    if (stereotype == null) {
      return;
    }

    StereotypesHelper.addStereotype(newElement, stereotype);
  }

  public void addStereotypeFields(Element newElement, ElementData xmlElement) {
    // Need more robust way of doing this including all fields of all stereotypes... eventually
    // Have list of properties for each stereotype, iterate through them and update if ElementData has
    // equivalent field
    if (newElement instanceof Constraint) {
      List<Stereotype> stereotypes = StereotypesHelper.getStereotypes(newElement);
      com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile validationProfile = StereotypesHelper.getProfile(project, "Validation Profile");
      Stereotype validationRule = StereotypesHelper.getStereotype(project, "validationRule", validationProfile);

      // Must check if constraint has validation rule stereotype that holds these attributes
      if (stereotypes.contains(validationRule)) {
        if (xmlElement.hasAttribute("severity")) {
          Enumeration severityKind = Finder.byQualifiedName().find(project, "UML Standard Profile::Validation Profile::SeverityKind");
          for (EnumerationLiteral el : severityKind.getOwnedLiteral()) {
            if (el.getName().contentEquals(xmlElement.getAttribute("severity"))) {
              StereotypesHelper.setStereotypePropertyValue(newElement, validationRule, "severity", el);
            }
          }
        }
        if (xmlElement.hasAttribute("errorMessage")) {
          StereotypesHelper.setStereotypePropertyValue(newElement, validationRule, "errorMessage", xmlElement.getAttribute("errorMessage"));
        }
      }
    }
  }

  @CheckForNull
  public static String idConversion(String id) {
    return getInstance().importIdToCameoId.get(id);
  }

  @CheckForNull
  public Element getImportedRelationshipEnd(String importId) {
    if (importId == null || importId.trim().isEmpty()) {
      return null;
    }

    // if (isImported(importId)) {
    // return getImportedElement(importId);
    // }

    // if (MtipUtils.isStandardLibraryElement(importId)) {
    // return MtipUtils.getStandardLibraryElement(importId);
    // }

    Element relationshipEnd = buildEntity(importId);

    if (relationshipEnd != null) {
      return relationshipEnd;
    }

    Logger.log(String.format("No element data found for relationship end. Checking profiles and model libraries for %s", importId));

    if (importId.startsWith("_9_")) {
      Element supplier = Finder.byQualifiedName().find(project, "UML Standard Profile::UML2 Metamodel::Class");

      if (supplier != null) {
        return supplier;
      }
    }

    if (importId.startsWith("_")) {
      return (Element) project.getElementByID(importId);
    }

    return null;
  }

  public void trackIds(Element newElement, ElementData modelElement) {
    importIdToCameoId.put(modelElement.getImportId(), MtipUtils.getId(newElement));
  }

  public HashMap<Element, Rectangle> getImportedElementsOnDiagram(ElementData modelElement) {
    HashMap<Element, Rectangle> elementsOnDiagram = new HashMap<Element, Rectangle>();

    for (String importId : modelElement.getDiagramElements()) {
      if (!isImported(importId)) {
        Logger.log(String.format("Element with import id %s failed to be created before populating diagram.", importId));
        continue;
      }

      Element elementOnDiagram = getImportedElement(importId);

      if (elementOnDiagram == null) {
        Logger.log(String.format("Failed to find created element with import id %s", importId));
        continue;
      }

      elementsOnDiagram.put(elementOnDiagram, modelElement.getLocation(importId));
    }

    return elementsOnDiagram;
  }

  public List<Element> getImportedRelationshipsOnDiagram(ElementData elementData) {
    List<Element> relationshipsOnDiagram = new ArrayList<Element>();

    for (String importId : elementData.getDiagramConnectors()) {
      if (!isImported(importId)) {
        buildEntity(importId);
      }

      Element relationshipOnDiagram = getImportedElement(importId);

      if (relationshipOnDiagram == null) {
        Logger.log(String.format("Failed to find created element with import id %s", importId));
        continue;
      }

      relationshipsOnDiagram.add(relationshipOnDiagram);
    }

    return relationshipsOnDiagram;
  }

  public void populateDiagram(AbstractDiagram diagram, Diagram newDiagram, ElementData elementData) {
    diagram.createDependentElements(elementData);

    diagram.elementsOnDiagram = getImportedElementsOnDiagram(elementData);
    diagram.relationshipsOnDiagram = getImportedRelationshipsOnDiagram(elementData);
    // diagram.noElementPresentationElements = getNoElementPresentationElements(modelElement);

    CameoUtils.createSession(project, String.format("Adding elements to diagram with type %s.", elementData.getType()));

    diagram.addElementsToDiagram(project, newDiagram);
    diagram.addRelationshipsToDiagram(project, newDiagram);

    if (diagram.hasNoPositionData()) {
      Application.getInstance().getProject().getDiagram(newDiagram).layout(true,
          new com.nomagic.magicdraw.uml.symbols.layout.ClassDiagramLayouter());
    }

    project.getDiagram(newDiagram).close();
    CameoUtils.closeSession(project);
  }

  public Set<String> getImportedElementIds() {
    return new HashSet<String>(importIdToCameoId.values());
  }

  public Set<String> getUnsupportedElementIds() {
    return unsupportedElementIds;
  }

  /**
   * Returns element by import id if and only if it has already been created. Otherwise, null.
   * 
   * @param importId Id of the element from the import data.
   * @return Element with the given import id if it has been created. Otherwise, null
   */
  @CheckForNull
  public Element getImportedElement(String importId) {
    return (Element) project.getElementByID(importIdToCameoId.get(importId));
  }

  @CheckForNull
  public static String getTypeFromImportId(String importId) {
    ElementData item = getInstance().allElementData.get(importId);

    if (item == null) {
      Logger.log(String.format("Unable to find element type from import id: %s", importId));
      return null;
    }

    return item.getType();
  }

  public static boolean isImportId(String id) {
    if (!getInstance().allElementData.containsKey(id)) {
      return false;
    }

    return true;
  }

  public void setPrimaryLocation(Element rootPackage) {
    if (rootPackage == null) {
      primaryLocation = project.getPrimaryModel();
    }

    primaryLocation = rootPackage;
  }

  boolean isImported(ElementData modelElement) {
    if (!importIdToCameoId.keySet().contains(modelElement.getImportId())) {
      return false;
    }

    return true;
  }

  boolean isImported(String id) {
    if (!importIdToCameoId.keySet().contains(id)) {
      return false;
    }

    return true;
  }
}
