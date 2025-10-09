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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.xml.parsers.ParserConfigurationException;
import org.aero.mtip.XML.XmlUtils;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.AbstractDiagram;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.metamodel.core.CommonElementsFactory;
import org.aero.mtip.metamodel.core.CommonRelationship;
import org.aero.mtip.metamodel.core.CommonRelationshipsFactory;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.FileSelect;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.MtipUtils;
import org.aero.mtip.util.TaggedValue;
import org.aero.mtip.util.XMLItem;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

  Map<String, String> importIdToCameoId = new HashMap<String, String>();
  Map<String, String> parentMap = new HashMap<String, String>();
  Set<String> unsupportedElementIds = new HashSet<String>();
  Set<String> invalidOwners = new HashSet<String>();

  HashMap<String, XMLItem> completeXML = new HashMap<String, XMLItem>();
  HashMap<String, XMLItem> stereotypesXML = new HashMap<String, XMLItem>();
  HashMap<String, XMLItem> profileXML = new HashMap<String, XMLItem>();
  List<XMLItem> properties = new ArrayList<XMLItem>();

  Project project;
  Element primaryLocation;
  CommonElementsFactory cef;
  CommonRelationshipsFactory crf;

  public static Importer createNewImporter() {
    instance = new Importer();
    return instance;
  }

  public static void destroy() {
    instance = null;
  }

  public static Importer getInstance() {
    return instance;
  }

  public static void importFromFiles(File[] files, Element parentPackage) throws ParserConfigurationException, IOException, SAXException {
    Importer importer = createNewImporter();

    for (File file : files) {
      Document doc = FileSelect.createDocument(file);
      doc.getDocumentElement().normalize();

      importer.parseXML(doc, parentPackage);
    }

    importer.createModel();

    Logger.logSummary(importer);
  }

  public Importer() {
    org.aero.mtip.profiles.Profile.clearAllProfiles();

    project = Application.getInstance().getProject();
    cef = new CommonElementsFactory();
    crf = new CommonRelationshipsFactory();

    Logger.createNewImportLogger();
  }

  public void parseXML(Document doc, Element start) throws NullPointerException {
    primaryLocation = start;

    if (primaryLocation == null) {
      primaryLocation = project.getPrimaryModel();
    }

    // Read file and set up XML object
    Node packet = doc.getDocumentElement();
    NodeList dataNodes = packet.getChildNodes();

    // Parse XML and build model based on data
    buildModelMap(dataNodes);
    buildStereotypesTree();
  }


  public void createModel() throws NullPointerException {
    project.getOptions().setAutoNumbering(false);

    buildModel(profileXML);
    buildModel(completeXML);

    CameoUtils.createSession(project, "Refresh");
    CameoUtils.closeSession(project);

    project.getOptions().setAutoNumbering(true);
  }

  public void buildModel(HashMap<String, XMLItem> parsedXML) {
    for (Entry<String, XMLItem> entry : parsedXML.entrySet()) {
      XMLItem modelElement = entry.getValue();
      String id = entry.getKey();

      if (modelElement == null) {
        Logger.log(String.format("Error parsing data for XML item with id %s. Element will not be imported", id));
        continue;
      }

      if (isImported(modelElement)) {
        continue;
      }

      buildEntity(parsedXML, modelElement);
    }
  }

  @CheckForNull
  public Element buildEntity(HashMap<String, XMLItem> parsedXML, XMLItem modelElement) {
    if (modelElement == null) {
      Logger.log(String.format("XML data not found attempting to build entity."));
      return null;
    }

    String category = modelElement.getCategory();

    if (category.isEmpty()) {
      Logger.log(String.format("Element %s with id %s is uncategorized and will not be imported.", modelElement.getName(),
          modelElement.getImportId()));
      return null;
    }

    if (category.equals(SysmlConstants.ELEMENT)) {
      return buildElement(parsedXML, modelElement);
    }

    if (category.equals(SysmlConstants.RELATIONSHIP)) {
      return buildRelationship(parsedXML, modelElement);
    }

    if (modelElement.getCategory().equals(SysmlConstants.DIAGRAM)) {
      return buildDiagram(parsedXML, modelElement);
    }

    return null;
  }

  @CheckForNull
  public Element buildDiagram(HashMap<String, XMLItem> parsedXML, XMLItem modelElement) {
    if (modelElement == null) {
      Logger.log("Missing XML data for model element.");
      return null;
    }

    if (isImported(modelElement)) {
      return getImportedElement(modelElement);
    }

    Element owner = getOwnerElement(modelElement, parsedXML);

    if (!MtipUtils.isSupportedDiagram(modelElement.getType())) {
      Logger.log(String.format("%s with id %s type is not supported. ", modelElement.getType(), modelElement.getImportId()));
      return null;
    }

    CameoUtils.createSession(project, String.format("Create %s Element", modelElement.getType()));

    AbstractDiagram diagram =
        (AbstractDiagram) cef.createElement(modelElement.getType(), modelElement.getName(), modelElement.getImportId());
    Diagram newDiagram = (Diagram) diagram.createElement(project, owner, modelElement);

    if (newDiagram == null) {
      Logger.log(String.format("Error creating diagram. Check CameoDiagramConstant for diagram with id: %s", modelElement.getImportId()));
      return null;
    }

    DiagramPresentationElement dpe = project.getDiagram(newDiagram);

    if (dpe == null) {
      Logger.log(String.format("Unable to find dpe. Cannot populate digram for element %s", modelElement.getImportId()));
      return newDiagram;
    }

    dpe.open();
    trackIds(newDiagram, modelElement);

    CameoUtils.closeSession(project);

    // Opens and closes its own session while populating the diagram
    populateDiagram(diagram, newDiagram, modelElement, parsedXML);
    return newDiagram;
  }

  @CheckForNull
  public Element buildRelationship(HashMap<String, XMLItem> parsedXML, XMLItem modelElement) {
    if (modelElement == null) {
      Logger.log("Missing XML data for model element on Importer.buildRelationship()");
      return null;
    }

    Element owner = getOwnerElement(modelElement, parsedXML);

    if (isImported(modelElement)) {
      return getImportedElement(modelElement);
    }

    if (!MtipUtils.isSupportedRelationship(modelElement.getType())) {
      Logger.log(String.format("%s type not supported. Import id %s", modelElement.getType(), modelElement.getImportId()));
      return null;
    }

    Element client = getImportedClient(modelElement, parsedXML);

    if (client == null) {
      Logger.log(String.format("Client null for relationship %s. Import id %s", modelElement.getName(), modelElement.getImportId()));
    }

    Element supplier = getImportedSupplier(modelElement, parsedXML);

    if (supplier == null) {
      Logger.log(String.format("Supplier null for relationship %s. Import id %s", modelElement.getName(), modelElement.getImportId()));
    }

    CameoUtils.createSession(project, String.format("Creating CommonRelationship of type %s.", modelElement.getType()));
    CommonRelationship relationship =
        crf.createElement(modelElement.getType(), modelElement.getAttribute("name"), modelElement.getImportId());
    CameoUtils.closeSession(project);

    if (relationship == null) {
      Logger.log("Error. Element missing in common elements factory.");
      return null;
    }

    relationship.createDependentElements(parsedXML, modelElement);

    CameoUtils.createSession(project, String.format("Creating Relationship of type %s.", modelElement.getType()));

    Element importedRelationship = relationship.createElement(project, owner, client, supplier, modelElement);

    if (importedRelationship == null) {
      Logger.log(String.format("Relationship failed to be created with id %s.", modelElement.getImportId()));
      return null;
    }

    if (importedRelationship.getOwner() == null) {
      Logger.log("Owner failed to be set including any default owners. Relationship not created.");
      invalidOwners.add(modelElement.getImportId());

      if (importedRelationship != null) {
        ModelHelper.dispose(Collections.singletonList(importedRelationship));
      }

      return null;
    }

    CameoUtils.closeSession(project);

    trackIds(importedRelationship, modelElement);
    relationship.createReferencedElements(parsedXML, modelElement);

    return importedRelationship;
  }

  @CheckForNull
  public Element buildElement(HashMap<String, XMLItem> parsedXML, XMLItem modelElement) {
    if (modelElement == null) {
      Logger.log("Missing XML data for element on Importer.buildElement()");
      return null;
    }

    Element owner = getOwnerElement(modelElement, parsedXML);

    if (isImported(modelElement)) {
      return getImportedElement(modelElement);
    }

    if (!MtipUtils.isSupportedElement(modelElement.getType())) {
      Logger.log(String.format("%s type not supported. Import id %s", modelElement.getType(), modelElement.getImportId()));
      return null;
    }

    CameoUtils.createSession(project, String.format("Initialize element %s Element", modelElement.getType()));
    CommonElement element = cef.createElement(modelElement.getType(), modelElement.getName(), modelElement.getImportId());
    CameoUtils.closeSession(project);

    element.createDependentElements(parsedXML, modelElement);

    // Create new session if tagged values or other dependent elements are found, built, and session is
    // closed.
    CameoUtils.createSession(project, String.format("Create %s with dependent Elements", modelElement.getType()));
    Element importedElement = element.createElement(project, owner, modelElement);

    if (importedElement == null) {
      Logger.log(String.format("Element failed to be created with id %s.", modelElement.getImportId()));
      return null;
    }

    if (importedElement.getOwner() == null) {
      Logger.log("Owner failed to be set including any default owners. Element with id " + modelElement.getImportId() + " not created.");
      unsupportedElementIds.add(modelElement.getImportId());
      ModelHelper.dispose(Collections.singletonList(importedElement));

      return null;
    }

    CameoUtils.closeSession(project);

    trackIds(importedElement, modelElement);
    addStereotypes(importedElement, modelElement);

    element.addStereotypeTaggedValues(modelElement);
    element.createReferencedElements(parsedXML, modelElement);

    CameoUtils.createSession(project, String.format("Add referenced elements to %s", modelElement.getType()));
    element.addReferences();
    CameoUtils.closeSession(project);

    // Logger.log(String.format("Created element %s of type: %s.", modelElement.getName(),
    // modelElement.getType()));
    return importedElement;
  }

  public void addStereotypes(Element newElement, XMLItem modelElement) {
    HashMap<String, String> stereotypes = modelElement.getStereotypes();

    for (String stereotype : stereotypes.keySet()) {
      addStereotype(newElement, stereotype, stereotypes.get(stereotype));
      addStereotypeFields(newElement, modelElement);
    }
  }

  public void buildModelMap(NodeList dataNodes) {
    int invalidElements = 0;
    int totalElementCount = 0;

    Logger.log(String.format("Parsing %d data nodes.", XmlUtils.asElementList(dataNodes).size()));

    for (Node dataNode : XmlUtils.asElementListWithTag(dataNodes, XmlTagConstants.DATA)) { // Loop through all of the <data> nodes
      XMLItem modelElement = parseElementData(dataNode);
      totalElementCount++;

      if (!modelElement.isValid()) {
        Logger.log(String.format("Invalid data for element: ", modelElement));
        invalidElements++;
        continue;
      }

      addToModelMap(modelElement);
    }

    checkProperties();
    Logger.log(String.format("Successfully parsed %d/%d data elements.", totalElementCount - invalidElements, totalElementCount));
  }

  public void addToModelMap(XMLItem modelElement) {
    for (String id : modelElement.getIds()) {
      completeXML.put(id, modelElement);
    }

    if (modelElement.isStereotype()) {
      for (String id : modelElement.getIds()) {
        stereotypesXML.put(id, modelElement);
      }
    }

    if (modelElement.isProperty() && modelElement.getParent() == null) { // Property parent may not exists yet. Check later if property of
                                                                         // stereotype.
      properties.add(modelElement);
    }

    if (completeXML.containsKey(modelElement.getParent()) && modelElement.isProperty()
        && completeXML.get(modelElement.getParent()).isStereotype()) {
      stereotypesXML.put(modelElement.getImportId(), modelElement);
    }
  }

  /**
   * Checks all cached XMLItems in properties to determine if they are properties of stereotypes and
   * required by stereotypesXML.
   */
  public void checkProperties() {
    for (XMLItem modelElement : properties) {
      if (modelElement.getParent() == null) {
        continue;
      }

      if (completeXML.get(modelElement.getParent()) == null) {
        continue;
      }

      if (completeXML.get(modelElement.getParent()).isStereotype()) {
        stereotypesXML.put(modelElement.getImportId(), modelElement);
        continue;
      }

      Logger.log(String.format("Cannot determine if property is property of stereotype: %s", modelElement));
    }
  }

  public XMLItem parseElementData(Node dataNode) {
    XMLItem modelElement = new XMLItem();

    getType(XmlUtils.getFirstChildByTagName(dataNode, XmlTagConstants.TYPE), modelElement);
    getIDs(XmlUtils.getFirstChildByTagName(dataNode, XmlTagConstants.ID), modelElement);
    getAttributes(XmlUtils.getFirstChildByTagName(dataNode, XmlTagConstants.ATTRIBUTES), modelElement);
    getRelationships(XmlUtils.getFirstChildByTagName(dataNode, XmlTagConstants.RELATIONSHIPS), modelElement);

    return modelElement;
  }

  public void getType(org.w3c.dom.Element typeElement, XMLItem modelElement) {
    if (typeElement == null) {
      Logger.log("Cannot get type from input data. No type element found.");
      return;
    }

    String type = typeElement.getTextContent();

    if (type == null) {
      Logger.log("Cannot get type from input data. No type string found in type element.");
      return;
    }

    if (!type.contains(".")) {
      Logger.log("Cannot get type from input data. Format of type string does not match convention <metamodel.<type>: %s, type");
      return;
    }

    modelElement.setType(type.split("[.]")[1]);

  }

  public void buildStereotypesTree() {
    for (Entry<String, XMLItem> entry : stereotypesXML.entrySet()) {
      XMLItem modelElement = entry.getValue();
      String id = entry.getKey();
      profileXML.put(id, modelElement);
      String parentID = modelElement.getParent();
      addParentToProfileXML(parentID);
    }
  }

  public void addParentToProfileXML(String parentID) {
    if (parentID != "") {
      profileXML.put(parentID, completeXML.get(parentID));
      XMLItem nextParent = completeXML.get(parentID);
      if (nextParent != null) {
        String nextParentID = nextParent.getParent();
        addParentToProfileXML(nextParentID);
      }
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

  public void addStereotypeFields(Element newElement, XMLItem xmlElement) {
    // Need more robust way of doing this including all fields of all stereotypes... eventually
    // Have list of properties for each stereotype, iterate through them and update if xmlElement has
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

  public static void getRelationships(Node fieldNode, XMLItem modelElement) {
    if (fieldNode == null) {
      Logger.log("Cannot get relationships. Relationships fieldNode is null.");
      return;
    }

    for (org.w3c.dom.Element relationshipNode : XmlUtils.asElementList(fieldNode.getChildNodes())) {
      if (relationshipNode.getNodeName() == XmlTagConstants.ELEMENT) {
        getAllDiagramElementData(relationshipNode, modelElement);
        continue;
      }

      if (relationshipNode.getNodeName() == XmlTagConstants.DIAGRAM_CONNECTOR) {
        getAllDiagramConnectors(relationshipNode, modelElement);
        continue;
      }

      if (relationshipNode.getNodeName() == XmlTagConstants.HAS_PARENT) {
        modelElement.setParent(getIdValueFromNode(relationshipNode));
        continue;
      }

      getRelationshipData(relationshipNode, modelElement);
    }
  }

  public static void getRelationshipData(Node relationshipNode, XMLItem modelElement) {
    if (XmlUtils.isDataTypeList(relationshipNode)) {
      getListRelationshipData(relationshipNode, modelElement);
      return;
    }

    Node idNode = XmlUtils.getFirstChildByTagName(relationshipNode, XmlTagConstants.ID);

    if (idNode == null) {
      Logger
          .log(String.format("Could not parse relationship node with tag %s. No nested id element found.", relationshipNode.getNodeName()));
      return;
    }

    String id = idNode.getTextContent();

    if (id == null || id.isEmpty()) {
      Logger.log(String.format("Could not parse relationship node with tag %s. Id element has null or empty text.",
          relationshipNode.getNodeName()));
      return;
    }

    modelElement.addAttribute(relationshipNode.getNodeName(), id);

  }

  public static void getListRelationshipData(Node relationshipNode, XMLItem modelElement) {
    for (Node listItemNode : XmlUtils.asElementList(relationshipNode.getChildNodes())) {
      Node idNode = XmlUtils.getFirstChildByTagName(listItemNode, XmlTagConstants.ID);

      if (idNode == null) {
        Logger.log(
            String.format("Could not parse relationship node with tag %s. No nested id element found.", relationshipNode.getNodeName()));
        return;
      }

      String id = idNode.getTextContent();

      if (id == null || id.isEmpty()) {
        Logger.log(String.format("Could not parse relationship node with tag %s. Id element has null or empty text.",
            relationshipNode.getNodeName()));
        return;
      }

      modelElement.addListAttribute(relationshipNode.getNodeName(), id);
    }
  }

  public static String getIdValueFromNode(Node relationship) {
    NodeList idNodes = relationship.getChildNodes();
    for (int k = 0; k < idNodes.getLength(); k++) {
      Node idNode = idNodes.item(k);
      if (idNode.getNodeType() == Node.ELEMENT_NODE) {
        if (idNode.getNodeName() == XmlTagConstants.ID_TAG) {
          return idNode.getTextContent();
        }
      }
    }
    Logger.log("No ID found in field " + relationship.getNodeName() + ".");
    return "";
  }

  public static void getAllDiagramElementData(Node relationshipNode, XMLItem modelElement) {
    for (org.w3c.dom.Element elementNode : XmlUtils.asElementList(relationshipNode.getChildNodes())) {
      getDiagramElementData(elementNode, modelElement);
    }
  }

  public static void getDiagramElementData(Node elementNode, XMLItem modelElement) {
    NodeList elementAttributesNodes = elementNode.getChildNodes();
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;
    int bottom = 0;
    int right = 0;

    String id = "";
    String type = "";
    String parentID = "";

    for (int i = 0; i < elementAttributesNodes.getLength(); i++) {
      Node elementAttributeNode = elementAttributesNodes.item(i);
      if (elementAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
        if (elementAttributeNode.getNodeName() == XmlTagConstants.RELATIONSHIP_METADATA) {
          NodeList positionNodes = elementAttributeNode.getChildNodes();
          for (int j = 0; j < positionNodes.getLength(); j++) {
            Node positionNode = positionNodes.item(j);
            if (positionNode.getNodeType() == Node.ELEMENT_NODE) {
              if (positionNode.getNodeName() == XmlTagConstants.TOP) {
                y = Integer.parseInt(positionNode.getTextContent());
              }
              if (positionNode.getNodeName() == XmlTagConstants.BOTTOM) {
                bottom = Integer.parseInt(positionNode.getTextContent());
              }
              if (positionNode.getNodeName() == XmlTagConstants.LEFT) {
                x = Integer.parseInt(positionNode.getTextContent());
              }
              if (positionNode.getNodeName() == XmlTagConstants.RIGHT) {
                right = Integer.parseInt(positionNode.getTextContent());
              }
              if (positionNode.getNodeName() == XmlTagConstants.DIAGRAM_PARENT) {
                NodeList parentNodes = positionNode.getChildNodes();
                for (int k = 0; k < parentNodes.getLength(); k++) {
                  Node parentNode = parentNodes.item(k);
                  if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
                    if (parentNode.getNodeName() == XmlTagConstants.ID) {
                      parentID = parentNode.getTextContent();
                    }
                  }
                }
              }
            }
          }
        }
        if (elementAttributeNode.getNodeName() == XmlTagConstants.ID) {
          id = elementAttributeNode.getTextContent();
        }
        if (elementAttributeNode.getNodeName() == XmlTagConstants.TYPE) {
          type = elementAttributeNode.getTextContent();
        }
      }
    }
    if (x != 0 && y != 0 && bottom != 0 && right != 0 && !id.isEmpty() && !type.isEmpty()) {
      modelElement.addChildElement(id);
      modelElement.addChildElementType(id, type);
      width = right - x;
      height = bottom - y;
      modelElement.addLocation(id, new Rectangle(x, -y, width, -height));
      if (!parentID.isEmpty()) {
        modelElement.addDiagramParent(id, parentID);
      }

    } else {
      modelElement.addChildElement(id);
      modelElement.addChildElementType(id, type);
      modelElement.addLocation(id, new Rectangle(-999, -999, -999, -999));
    }
  }

  public static void getAllDiagramConnectors(Node relationshipNode, XMLItem modelElement) {
    for (Node elementNode : XmlUtils.asElementList(relationshipNode.getChildNodes())) {
      getDiagramRelationshipData(elementNode, modelElement);
    }
  }

  public static void getDiagramRelationshipData(Node relationshipNode, XMLItem modelElement) {
    org.w3c.dom.Element idElement = XmlUtils.getFirstChildByTagName(relationshipNode, XmlTagConstants.ID);
    org.w3c.dom.Element typeElement = XmlUtils.getFirstChildByTagName(relationshipNode, XmlTagConstants.TYPE);

    if (idElement == null || typeElement == null) {
      Logger.log(String.format("Cannot parse diagram connector data. No %s or %s tag found in diagramConnector.", XmlTagConstants.ID,
          XmlTagConstants.TYPE));
      return;
    }

    String id = idElement.getTextContent();

    if (id == null) {
      Logger.log(String.format("Cannot parse diagram connector data. Text of %s element is null.", XmlTagConstants.ID));
      return;
    }

    modelElement.addChildRelationship(id);
  }

  public static Node getSubAttribute(Node attributeNode) {
    NodeList childNodes = attributeNode.getChildNodes();
    for (int k = 0; k < childNodes.getLength(); k++) {
      Node childNode = childNodes.item(k);
      if (childNode.getNodeName().contentEquals(XmlTagConstants.ATTRIBUTE)) {
        return childNode;
      }
    }
    return null;
  }

  public static List<Node> getListSubAttributes(Node attributeNode) {
    List<Node> nodes = new ArrayList<Node>();
    NodeList childNodes = attributeNode.getChildNodes();
    for (int k = 0; k < childNodes.getLength(); k++) {
      Node childNode = childNodes.item(k);
      if (childNode.getNodeName().contentEquals(XmlTagConstants.ATTRIBUTE)) {
        nodes.add(childNode);
      }
    }
    if (!nodes.isEmpty()) {
      return nodes;
    }
    return null;
  }

  public static void getAttributes(Node fieldNode, XMLItem modelElement) {
    if (fieldNode == null) {
      Logger.log("Cannot parse attributes. Attributes fieldNode is null.");
      return;
    }

    for (Node attributeNode : XmlUtils.asElementList(fieldNode.getChildNodes())) {
      if (XmlUtils.isStereotypeAttribute(attributeNode)) {
        getStereotype(attributeNode, modelElement);
        continue;
      }

      if (XmlUtils.isStereotypeTaggedValueAttribute(attributeNode)) {
        getTaggedValue(attributeNode, modelElement);
        continue;
      }

      if (XmlUtils.isDataTypeList(attributeNode)) {
        for (Node listAttribute : getListSubAttributes(attributeNode)) {
          modelElement.addListAttribute(XmlUtils.getKey(listAttribute), listAttribute.getTextContent());
        }

        continue;

      }
      // TODO: Ensure generic attribute handling replaces specific constrainedElement handling
      Node subAttribute = getSubAttribute(attributeNode);
      modelElement.addAttribute(XmlUtils.getKey(attributeNode), subAttribute.getTextContent());


    }
  }

  public static void getStereotype(Node attributeNode, XMLItem modelElement) {
    org.w3c.dom.Element stereotypeNameElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE_NAME);
    org.w3c.dom.Element profileNameElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.ATTRIBUTE_KEY_PROFILE_NAME);

    if (stereotypeNameElement == null) {
      Logger.log(String.format("Cannot parse stereotype. Missing %s element within %s", XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE_NAME,
          XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE));
      return;
    }

    if (profileNameElement == null) {
      Logger.log(String.format("Cannot parse stereotype. Missing %s element within %s", XmlTagConstants.ATTRIBUTE_KEY_PROFILE_NAME,
          XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE));
      return;
    }

    String stereotypeName = stereotypeNameElement.getTextContent();
    String profileName = profileNameElement.getTextContent();

    if (stereotypeName == null || stereotypeName.isEmpty()) {
      Logger.log("Cannot parse stereotype. Stereotype name is null or empty.");
      return;
    }

    if (profileName == null || profileName.isEmpty()) {
      Logger.log("Cannot parse stereotype. Profile name for stereotype is null or empty.");
      return;
    }

    modelElement.addStereotype(stereotypeName, profileName);
  }

  public static void getTaggedValue(Node attributeNode, XMLItem modelElement) {
    org.w3c.dom.Element stereotypeNameElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE_NAME);
    org.w3c.dom.Element profileNameElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.ATTRIBUTE_KEY_PROFILE_NAME);
    org.w3c.dom.Element taggedValueNameElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.TAGGED_VALUE_NAME);
    org.w3c.dom.Element taggedValueValueTypeElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.TAGGED_VALUE_TYPE);
    org.w3c.dom.Element taggedValueValuesElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.TAGGED_VALUE_VALUES);
    org.w3c.dom.Element taggedValueValueElement = XmlUtils.getFirstChildByKey(taggedValueValuesElement, XmlTagConstants.TAGGED_VALUE_VALUE);
    
    if (stereotypeNameElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE_NAME, XmlTagConstants.STEREOTYPE_TAGGED_VALUE));
      return;
    }
    
    if (profileNameElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.ATTRIBUTE_KEY_PROFILE_NAME, XmlTagConstants.STEREOTYPE_TAGGED_VALUE));
      return;
    }
    
    if (taggedValueNameElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.TAGGED_VALUE_NAME, XmlTagConstants.STEREOTYPE_TAGGED_VALUE));
      return;
    }
    
    if (taggedValueValueTypeElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.TAGGED_VALUE_TYPE, XmlTagConstants.STEREOTYPE_TAGGED_VALUE));
      return;
    }
    
    if (taggedValueValuesElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.TAGGED_VALUE_VALUES, XmlTagConstants.STEREOTYPE_TAGGED_VALUE));
      return;
    }
    
    if (taggedValueValueElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.TAGGED_VALUE_VALUE, XmlTagConstants.TAGGED_VALUE_VALUES));
      return;
    }

    TaggedValue tv = new TaggedValue(stereotypeNameElement.getTextContent(), profileNameElement.getTextContent(), taggedValueNameElement.getTextContent(), taggedValueValueTypeElement.getTextContent(), getTaggedValueValue(taggedValueValueElement));
    
    if (!tv.isValid()) {
      return;
    }
    
    modelElement.addStereotypeTaggedValue(tv);
  }
  
  public static String getTaggedValueValue(org.w3c.dom.Element tvve) {
    // TODO: Add check for list values and return a list of values.
    return tvve.getTextContent();
  }

  public static void getIDs(Node fieldNode, XMLItem modelElement) {
    if (fieldNode == null) {
      Logger.log("Cannot parse IDs. No id element found in data element.");
      return;
    }

    for (Node idNode : XmlUtils.asElementList(fieldNode.getChildNodes())) {
      modelElement.addId(idNode.getTextContent());
      modelElement.addIdWithType(idNode.getNodeName(), idNode.getTextContent());

      if (idNode.getNodeName().contentEquals(XmlTagConstants.HUDDLE_ID)) {
        modelElement.setImportId(idNode.getTextContent());
      }
    }

    if (modelElement.getImportId().trim().isEmpty() && !modelElement.getIds().isEmpty()) {
      modelElement.setImportId(modelElement.getIds().get(0));
    }
  }

  @CheckForNull
  public static String idConversion(String id) {
    return getInstance().importIdToCameoId.get(id);
  }

  @CheckForNull
  public Element getOwnerElement(XMLItem modelElement, HashMap<String, XMLItem> parsedXML) {
    if (modelElement == null) {
      return null;
    }

    String ownerID = modelElement.getParent();
    XMLItem ownerElement = parsedXML.get(ownerID);

    if (modelElement.getParent().trim().isEmpty() || ownerElement == null) {
      if (modelElement.getType() != SysmlConstants.MODEL) {
        Logger.log(String.format("Xml object not found for owner of %s with id %s.", modelElement.getName(), modelElement.getImportId()));
      }

      return primaryLocation; // Set owned equal to Primary model if no hasParent attribute in XML -> parent field in XMLItem ==
                              // ""
    }

    String ownerCreatedID = idConversion(ownerID);

    if (ownerCreatedID == null || ownerCreatedID.trim().isEmpty()) {
      return buildEntity(parsedXML, ownerElement);
    }

    return (Element) project.getElementByID(ownerCreatedID);

  }

  @CheckForNull
  public Element getImportedClient(XMLItem modelElement, HashMap<String, XMLItem> parsedXML) {
    if (modelElement.getClient() == null || modelElement.getClient().trim().isEmpty()) {
      Logger.log(String.format("No client id in xml for %s with id %s.", modelElement.getName(), modelElement.getImportId()));
      return null;
    }

    if (isImported(modelElement.getClient())) {
      return (Element) getImportedElement(modelElement.getClient());
    }

    if (MtipUtils.isStandardLibraryElement(modelElement.getClient(), modelElement.getClientType())) {
      return MtipUtils.getStandardLibraryElement(modelElement.getClient());
    }

    String clientImportId = modelElement.getClient();
    XMLItem clientElement = parsedXML.get(clientImportId);

    if (clientElement != null) {
      return buildEntity(parsedXML, clientElement);
    }

    Logger.log(String.format(
        "Client Element with id : %s not exported with XML. " + "Checking for elements from cameo profiles and model libraries",
        clientImportId));

    if (clientImportId.startsWith("_9_")) {
      Element client = Finder.byQualifiedName().find(project, "UML Standard Profile::UML2 Metamodel::Class");
      if (client != null) {
        return client;
      }
    }

    if (clientImportId.startsWith("_")) {
      return (Element) project.getElementByID(clientImportId);
    }

    return null;
  }

  public Element getImportedSupplier(XMLItem modelElement, HashMap<String, XMLItem> parsedXML) {
    if (modelElement.getSupplier() == null || modelElement.getSupplier().trim().isEmpty()) {
      Logger.log(String.format("No supplier id in xml for %s with id %s.", modelElement.getName(), modelElement.getImportId()));
      return null;
    }

    if (parentMap.containsKey(modelElement.getSupplier())) {
      return (Element) project.getElementByID(parentMap.get(modelElement.getSupplier()));
    }

    if (MtipUtils.isStandardLibraryElement(modelElement.getClient(), modelElement.getClientType())) {
      return MtipUtils.getStandardLibraryElement(modelElement.getSupplier());
    }

    String supplierImportId = modelElement.getSupplier();
    XMLItem supplierElement = parsedXML.get(supplierImportId);

    if (supplierElement != null) {
      return buildEntity(parsedXML, supplierElement);
    }

    Logger.log(String.format(
        "Supplier element with id : %s not exported with XML. " + "Checking for elements from cameo profiles and model libraries",
        supplierImportId));

    if (supplierImportId.startsWith("_9_")) {
      Element supplier = Finder.byQualifiedName().find(project, "UML Standard Profile::UML2 Metamodel::Class");
      if (supplier != null) {
        return supplier;
      }
    }

    if (supplierImportId.startsWith("_")) {
      return (Element) project.getElementByID(supplierImportId);
    }

    return null;
  }

  public void trackIds(Element newElement, XMLItem modelElement) {
    importIdToCameoId.put(modelElement.getImportId(), MtipUtils.getId(newElement));
  }

  public HashMap<Element, Rectangle> getImportedElementsOnDiagram(XMLItem modelElement, HashMap<String, XMLItem> parsedXML) {
    HashMap<Element, Rectangle> elementsOnDiagram = new HashMap<Element, Rectangle>();

    for (String importId : modelElement.getChildElements(parsedXML)) {
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

  public List<Element> getImportedRelationshipsOnDiagram(XMLItem modelElement, HashMap<String, XMLItem> parsedXML) {
    List<Element> relationshipsOnDiagram = new ArrayList<Element>();

    for (String importId : modelElement.getChildRelationships(parsedXML)) {
      if (!isImported(importId)) {
        Logger.log(String.format("Element with import id %s failed to be created before populating diagram.", importId));
        continue;
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

  public void populateDiagram(AbstractDiagram diagram, Diagram newDiagram, XMLItem modelElement, HashMap<String, XMLItem> parsedXML) {
    diagram.createDependentElements(parsedXML, modelElement);

    HashMap<Element, Rectangle> elementsOnDiagram = getImportedElementsOnDiagram(modelElement, parsedXML);
    List<Element> relationshipsOnDiagram = getImportedRelationshipsOnDiagram(modelElement, parsedXML);

    CameoUtils.createSession(project, String.format("Adding elements to diagram with type %s.", modelElement.getType()));

    diagram.addElements(project, newDiagram, elementsOnDiagram, modelElement);
    diagram.addRelationships(project, newDiagram, relationshipsOnDiagram);

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

  Element getImportedElement(XMLItem modelElement) {
    return (Element) project.getElementByID(importIdToCameoId.get(modelElement.getImportId()));
  }

  public Element getImportedElement(String importId) {
    return (Element) project.getElementByID(importIdToCameoId.get(importId));
  }

  @CheckForNull
  public static String getTypeFromImportId(String importId) {
    XMLItem item = getInstance().completeXML.get(importId);

    if (item == null) {
      return null;
    }

    Logger.log(String.format("Returning type from XMLItem: %s", item.toString()));
    return item.getType();
  }

  public static boolean isImportId(String id) {
    if (!getInstance().completeXML.containsKey(id)) {
      return false;
    }

    return true;
  }

  boolean isImported(XMLItem modelElement) {
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
