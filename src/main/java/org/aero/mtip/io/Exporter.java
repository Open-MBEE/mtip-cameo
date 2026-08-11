/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.io;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.metamodel.core.CommonElementsFactory;
import org.aero.mtip.metamodel.core.CommonRelationship;
import org.aero.mtip.metamodel.core.CommonRelationshipsFactory;
import org.aero.mtip.profiles.MagicDraw;
import org.aero.mtip.profiles.UAF;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.MtipUtils;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Comment;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Relationship;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.TypedElement;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;

public class Exporter {
  private static Exporter exporter;
  Project project;
  Package exportRoot;

  HashSet<String> exportedElements = new HashSet<String>();
  HashSet<String> implicitElements = new HashSet<String>();
  HashSet<String> unsupportedElements = new HashSet<String>();

  CommonElementsFactory cef;
  CommonRelationshipsFactory crf;

  boolean isUafModel = false;

  public static void exportModel(File file) throws IOException {
    exportModelFromPackage(file, null);
  }

  public static void exportModelFromPackage(File file, Package packageElement) {
    XmlWriter.initialize();

    exporter = new Exporter();
    exporter.buildXML(file, packageElement);
    
    Logger.logSummary(exporter);
  }

  public static void exportModelFromDiagram(File file, DiagramPresentationElement diagramPresentationElement) {
    XmlWriter.initialize();

    exporter = new Exporter();
    exporter.buildXMLFromDiagram(file, diagramPresentationElement);
  }

  public Exporter() {
    org.aero.mtip.profiles.Profile.clearAllProfiles();

    project = Application.getInstance().getProject();
    cef = new CommonElementsFactory();
    crf = new CommonRelationshipsFactory();

    Logger.createNewExportLogger();

    Logger.logMetadata();
    Logger.logConfigOptions();
  }

  public void buildXML(File file, Package packageElement) {
    if (packageElement == null) {
      packageElement = project.getPrimaryModel();
    }

    this.exportRoot = packageElement;
    exportPackageRecursive((Package) packageElement);
  }

  public void buildXMLFromDiagram(File file, DiagramPresentationElement diagramPresentationElement) {
    Element diagramElement = diagramPresentationElement.getElement();
    exportElementRecursiveUp(diagramElement);

    Logger.log(String.format("Attempting export of %d used model elements.", diagramPresentationElement.getUsedModelElements().size()));

    for (Element element : diagramPresentationElement.getUsedModelElements()) {
      exportElementRecursiveUp(element);
    }

    Logger.logSummary(this);
  }

  public void exportDiagramElementRecursive(File file, PresentationElement presentationElement) {
    List<PresentationElement> presentationElements = presentationElement.getPresentationElements();
    for (int i = 0; i < presentationElements.size(); i++) {
      Element element = presentationElements.get(i).getElement();

      if (element == null) {
        continue;
      }

      exportElementRecursiveUp(element);
    }

    Logger.logSummary(this);
  }

  /**
   * 
   * @param element Element to be exported. This begins at an arbitrary level of nested within the
   *        model.
   */
  public void exportElementRecursiveUp(Element element) {
    if (element == null) {
      return;
    }

    exportElementRecursiveUp(element.getOwner());

    if (element instanceof Package) {
      exportPackage(element);
      return;
    }

    exportEntity(element);
  }

  public void exportPackageRecursive(Package pkg) {
    if (pkg == null) {
      return;
    }

    exportPackage(pkg);

    for (Package nextPackage : pkg.getNestedPackage()) {
      if (!isSupportedPackage(nextPackage, isProfileExport())) {
        continue;
      }

      exportPackageRecursive(nextPackage);
    }

    for (Element element : pkg.getOwnedElement()) {
      if (isPackage(element)) {
        continue;
      }

      exportElementRecursive(element);
    }
  }

  public void exportElementRecursive(Element element) {
    if (element == null) {
      return;
    }

    exportEntity(element);

    for (Element ownedElement : element.getOwnedElement()) {
      if (ownedElement instanceof Package || (element instanceof Relationship && ownedElement instanceof Property)) {
        continue;
      }

      exportElementRecursive(ownedElement);
    }
  }

  public void exportPackage(Element pkg) {
    if (exportedElements.contains(MtipUtils.getId(pkg))) {
      return;
    }

    CommonElementsFactory cef = new CommonElementsFactory();
    String packageType = MtipUtils.getPackageType(pkg);

    CommonElement commonElement = cef.createElement(packageType, ((NamedElement) pkg).getName(), MtipUtils.getId(pkg));
    commonElement.writeToXML(pkg);

    exportedElements.add(MtipUtils.getId(pkg));
  }

  public void exportEntity(Element element) {
    if (element == null || exportedElements.contains(MtipUtils.getId(element))) {
      return;
    }

    if (MtipUtils.isImplicitlySupported(element, isProfileExport())) {
      addImplicitElement(element);
      return;
    }

    if (isExplicitlyUnsupported(element)) {
      unsupportedElements.add(MtipUtils.getId(element));
      Logger.log(String.format("%s is explicitly unsupported.", MtipUtils.getCameoElementType(element)));
      return;
    }

    String commonElementType = MtipUtils.getEntityType(element);

    if (commonElementType == null) {
      unsupportedElements.add(MtipUtils.getId(element));
      Logger.log(String.format("%s type could not be identified. Not currently supported.", MtipUtils.getCameoElementType(element)));
      return;
    }

    boolean isReferencedElement = MtipUtils.isReferencedElement(element);

    if (isReferencedElement) {
      Logger.log(String.format("%s is a referenced element from project %s.", CameoUtils.getElementName(element),
          Project.getProject(element).getHumanName()));
    }


    if (MtipUtils.isSupportedElement(commonElementType)) {
      exportElement(element, commonElementType);
      return;
    }

    if (MtipUtils.isSupportedRelationship(commonElementType)) {
      exportRelationship(element, commonElementType);
      return;
    }

    if (MtipUtils.isSupportedDiagram(commonElementType)) {
      exportElement(element, commonElementType);
      return;
    }

    unsupportedElements.add(MtipUtils.getId(element));
    Logger.log(String.format("%s is not categorized as an element, relationship, or diagram.", commonElementType));
  }

  public void exportElement(Element element, String elementType) {
    if (elementType == null) {
      Logger.log(String.format("Element type not found for %s with id %s", element.getHumanName(), MtipUtils.getId(element)));
      return;
    }

    CommonElement commonElement = cef.createElement(elementType, CameoUtils.getElementName(element), MtipUtils.getId(element));

    if (commonElement == null) {
      return;
    }

    commonElement.writeToXML(element);
    exportedElements.add(MtipUtils.getId(element));

    exportReferencedElements(element);
  }

  public void exportRelationship(Element element, String relationshipType) {
    if (relationshipType == null) {
      Logger.log(String.format("Relationship type not found for %s with id %s", element.getHumanName(), MtipUtils.getId(element)));
      return;
    }

    CommonRelationship commonRelationship =
        crf.createElement(relationshipType, CommonRelationship.getName(element), MtipUtils.getId(element));

    if (commonRelationship == null) {
      Logger.log(String.format("CommonRelationship not defined in CommonRelationshipFactory. Please check implementation for %s",
          relationshipType));
      return;
    }

    commonRelationship.writeToXML(element);
    exportedElements.add(MtipUtils.getId(element));

    // Check if supplier and client are created - important for UML Metaclasses and SysML Profile
    // objects referenced in extension and generalization relationships
    if (commonRelationship.getSupplier() != null) {
      exportEntity(commonRelationship.getSupplier());
    } else {
      Logger.log(String.format("Supplier is null for commonRelationship type %s", commonRelationship.getMetamodelConstant()));
    }

    if (commonRelationship.getClient() != null) {
      exportEntity(commonRelationship.getClient());
    } else {
      Logger.log(String.format("Client is null for commonRelationship type %s", commonRelationship.getMetamodelConstant()));
    }
  }

  public void exportReferencedElements(Element element) {
    if (element instanceof TypedElement) {
      Type type = ((TypedElement) element).getType();

      if (type == null) {
        return;
      }

      if (CameoUtils.isPrimitiveValueType(element)) {
        return;
      }

      if (CameoUtils.isPredefinedElement(type)) {
        return;
      }

      exportElementRecursiveUp(type);
    }
  }

  public String getPackageType(Element pkg) {
    if (CameoUtils.isModel(pkg)) {
      return SysmlConstants.MODEL;
    }

    if (CameoUtils.isProfile(pkg)) {
      return SysmlConstants.PROFILE;
    }

    return SysmlConstants.PACKAGE;
  }

  public boolean isExplicitlyUnsupported(Element element) {
    if ((element instanceof Comment && !UAF.isDefinition(element)) || MagicDraw.hasAdditionalPackageImportStereotype(element)) {
      return true;
    }

    return false;
  }

  public boolean isPackage(Element element) {
    if (element instanceof Package || element.getHumanName().equals("Profile Application") || element instanceof Profile) {
      return true;
    }

    return false;
  }

  /***
   * Determines if the given package is supported for exporting.
   * 
   * @param pkg Package for export.
   * @param isProfileExport boolean override to allow export of auxiliary resources.
   * @return True if the given package is supported.
   */
  public boolean isSupportedPackage(Package pkg, boolean isProfileExport) {
    if (isProfileExport) {
      return true;
    }


    return !isExternalPackage(pkg);
  }

  public boolean isExternalPackage(Package pkg) {
    if (StereotypesHelper.hasStereotype(pkg, MagicDraw.getAuxiliaryResourceStereotype())
        || pkg.getHumanName().equals("Package Unit Imports")) {
      return true;
    }

    return false;
  }

  public boolean isProfileExport() {
    if (MtipUtils.isChildOfAuxiliaryResource(exportRoot)) {
      return true;
    }

    return false;
  }

  /**
   * Tracks elements not explicitly exported (with their own data tag) to check sum with explicit
   * elements and total exported elements where: Total = implicit + explicit
   */
  public void addImplicitElement(Element element) {
    implicitElements.add(MtipUtils.getId(element));
  }

  public HashSet<String> getExportedElements() {
    return exportedElements;
  }

  public HashSet<String> getImplicitElements() {
    return implicitElements;
  }

  public HashSet<String> getUnsupportedElements() {
    return unsupportedElements;
  }
  
  public static CommonRelationshipsFactory getCommonRelationshipsFactory() {
    return exporter.crf;
  }
}
