/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.internalblock;

import java.util.Arrays;
import java.util.List;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonRelationship;
import org.aero.mtip.profiles.SysML;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.ElementData;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.MtipUtils;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Association;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.ConnectableElement;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.ConnectorEnd;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class Connector extends CommonRelationship {
  public Connector(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.xmlConstant = XmlTagConstants.CONNECTOR;
    this.metamodelConstant = SysmlConstants.CONNECTOR;
    this.element = f.createConnectorInstance();

    this.attributeDependencies.addAll(Arrays.asList(XmlTagConstants.SUPPLIER_PART_WITH_PORT, XmlTagConstants.CLIENT_PART_WITH_PORT));
  }

  @Override
  public Element createElement(Project project, Element owner, Element client, Element supplier, ElementData xmlElement) {
    com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector connector =
        (com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector) element;

    setOwner(owner);
    // Cameo considers client as the second connector end [ends.get(1)] and supplier as first. Must
    // reverse to fit supplier/client defined in MTIP.
    try {
      ModelHelper.setClientElement(element, supplier);
      ModelHelper.setSupplierElement(element, client);

    } catch (ClassCastException cce) {
      Logger.log("Invalid supplier/client for connector with id: " + this.importId + ". Supplier/client must be ConnectableElements.");
      ModelHelper.dispose(Arrays.asList(element));
      return null;
    }

    Profile sysmlProfile = StereotypesHelper.getProfile(project, "SysML");
    Stereotype nestedConnectorEndStereotype = StereotypesHelper.getStereotype(project, "NestedConnectorEnd", sysmlProfile);
    Stereotype elementPropertyPathStereotype = StereotypesHelper.getStereotype(project, "ElementPropertyPath", sysmlProfile);

    // if(supplier instanceof ConnectableElement && client instanceof ConnectableElement) {
    // connector.getEnd().get(0).setRole((ConnectableElement) supplier);
    // connector.getEnd().get(1).setRole((ConnectableElement) client);
    // } else {
    // Logger.log("Unable to create connector with id: " + this.importId + ". Supplier or client not a
    // ConnectableElement (Part, port, etc.)");
    // sysmlElement.dispose();
    // return null;
    // }

    ConnectorEnd firstMemberEnd = connector.getEnd().get(0);
    ConnectorEnd secondMemberEnd = connector.getEnd().get(1);
    
    // Roles will be overwritten by Part with Port if found.
    firstMemberEnd.setRole((ConnectableElement) supplier);
    secondMemberEnd.setRole((ConnectableElement) client);

    Element supplierPart = Importer.getInstance().getImportedElement(xmlElement.getAttribute(XmlTagConstants.SUPPLIER_PART_WITH_PORT));
    Element clientPart = Importer.getInstance().getImportedElement(xmlElement.getAttribute(XmlTagConstants.CLIENT_PART_WITH_PORT));

    if (supplierPart == null) {
     Logger.log("Could not set supplier part for connector. Supplier Part is null.") ;
     return element;
    }
    
    if (clientPart == null) {
      Logger.log("Could not set supplier part for connector. Client Part is null.") ;
      return element;
    }
    
    firstMemberEnd.setPartWithPort((com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) supplierPart);
    firstMemberEnd.setRole((ConnectableElement) supplier);
    StereotypesHelper.addStereotype(firstMemberEnd, nestedConnectorEndStereotype);
    StereotypesHelper.setStereotypePropertyValue(firstMemberEnd, elementPropertyPathStereotype, "propertyPath", supplierPart);
   
    
    secondMemberEnd.setPartWithPort((com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) clientPart);
    secondMemberEnd.setRole(
        ((List<ConnectorEnd>) ((com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) clientPart).get_connectorEndOfPartWithPort())
            .get(0).getRole());
    StereotypesHelper.addStereotype(secondMemberEnd, nestedConnectorEndStereotype);
    StereotypesHelper.setStereotypePropertyValue(secondMemberEnd, elementPropertyPathStereotype, "propertyPath", clientPart);

    Element typeElement = Importer.getInstance().getImportedElement(xmlElement.getAttribute(XmlTagConstants.TYPED_BY));
    
    if (typeElement != null) {
      try {
        connector.setType((Association) typeElement);
        CameoUtils.logGui("Connector type set to element with id " + MtipUtils.getId(typeElement));
      } catch (ClassCastException cce) {
        CameoUtils.logGui("Connector type is not an association. Type not set for connector with id " + this.importId);
      }
    }

    return connector;
  }

  @Override
  public void setOwner(Element owner) {
    if (owner == null || !(SysML.isBlock(owner))) {
      owner = CameoUtils.findNearestBlock(project, owner);
    }

    if (owner == null) {
      Logger.log(String.format(
          "Invalid parent. Parent must be block %s with id %s. No parents found in ancestors. Element could not be placed in model.", name,
          importId));
      return;
    }

    try {
      element.setOwner(owner);
    } catch (IllegalArgumentException iae) {
      Logger
          .log(String.format("Invalid parent. Parent must be block %s with id %s. Element could not be placed in model.", name, importId));
    }
  }

  @Override
  public Element getSupplier() {
    if (element instanceof com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector) {
      List<ConnectorEnd> connectorEnds =
          ((com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector) element).getEnd();
      if (connectorEnds.size() > 1) {
        return connectorEnds.get(0).getRole();
      }
    }
    return null;
  }

  @Override
  public Element getClient() {
    if (element instanceof com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector) {
      List<ConnectorEnd> connectorEnds =
          ((com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector) element).getEnd();
      if (connectorEnds.size() > 1) {
        return connectorEnds.get(1).getRole();
      }
    }
    return null;
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    writeSupplierPartWithPort(relationships, element);
    writeClientPartWithPort(relationships, element);
    writeConnectorType(relationships, element);

    return data;
  }

  private void writeSupplierPartWithPort(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector connector =
        (com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector) element;
    Element supplierPart = connector.getEnd().get(0).getPartWithPort();

    if (supplierPart == null) {
      return;
    }

    org.w3c.dom.Element supplierPartWithPortTag = XmlWriter.createMtipRelationship(supplierPart, XmlTagConstants.SUPPLIER_PART_WITH_PORT);
    XmlWriter.add(relationships, supplierPartWithPortTag);
  }

  private void writeClientPartWithPort(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector connector =
        (com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector) element;
    Element clientPart = connector.getEnd().get(1).getPartWithPort();

    if (clientPart == null) {
      return;
    }

    org.w3c.dom.Element clientPartWithPortTag = XmlWriter.createMtipRelationship(clientPart, XmlTagConstants.CLIENT_PART_WITH_PORT);
    XmlWriter.add(relationships, clientPartWithPortTag);
  }

  private void writeConnectorType(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector connector =
        (com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector) element;
    Association type = connector.getType();

    if (type == null) {
      return;
    }

    org.w3c.dom.Element typedByTag = XmlWriter.createMtipRelationship(type, XmlTagConstants.TYPED_BY);
    XmlWriter.add(relationships, typedByTag);
  }
}
