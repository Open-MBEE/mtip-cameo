/*
 * The Aerospace Corporation Huddle_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.core;

import java.util.Arrays;
import java.util.HashMap;
import javax.annotation.CheckForNull;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.MtipUtils;
import org.aero.mtip.util.XMLItem;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DirectedRelationship;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;

public abstract class CommonRelationship extends CommonElement {
  public static String INVALID_CLIENT_SUPPLIER_MESSAGE =
      "Invalid Client or Supplier - Not SysML Compliant";

  public CommonRelationship(String name, String EAID) {
    super(name, EAID);
    this.f = Application.getInstance().getProject().getElementsFactory();

  }

  public Element createElement(Project project, Element owner, Element client, Element supplier,
      XMLItem xmlElement) {
    try {
      super.createElement(project, owner, xmlElement);
      setSupplier(supplier);
      setClient(client);
      return element;
    } catch (ClassCastException cce) {
      String logMessage =
          "Invalid client/supplier for relationship " + name + " with id " + EAID + ".";
      CameoUtils.logGui(logMessage);
      Logger.log(logMessage);
      ModelHelper.dispose(Arrays.asList(element));
      return null;
    } catch (IllegalArgumentException iae) {
      String logMessage = "Invalid parent. Parent invalid for element " + name + " with id " + EAID
          + ". Supplier and client are also invalid parents. Element could not be placed in model.";
      CameoUtils.logGui(logMessage);
      Logger.log(logMessage);
      ModelHelper.dispose(Arrays.asList(element));
      return null;
    }
  }

  @Override
  public void setOwner(Element owner) {
    if (element == null) {
      Logger.log(String.format("Failed to create element with id %s. Cannot set owner.", EAID));
      return;
    }

    if (owner != null && ModelHelper.canMoveChildInto(owner, element)) {
      element.setOwner(owner);
      return;
    }
    
    Element supplier = getSupplier();

    if (supplier != null && ModelHelper.canMoveChildInto(supplier, element)) {
      Logger.log(String.format("Setting supplier as owner for %s with id %s.",
          element.getHumanType(), EAID));
      element.setOwner(supplier);
      return;
    }
    
    Element client = getClient();

    if (client != null && ModelHelper.canMoveChildInto(client, element)) {
      Logger.log(String.format("Setting client as owner for %s with id %s.",
          element.getHumanType(), EAID));
      element.setOwner(client);
    }
  }

  public org.w3c.dom.Element createBaseXML(Element element, Project project, Document xmlDoc) {
    return null;
  }

  public void createDependentElements(HashMap<String, XMLItem> parsedXML, XMLItem modelElement) {

  }

  public org.w3c.dom.Element getAttributes(NodeList dataNodes) {
    org.w3c.dom.Element attributes = null;
    for (int i = 0; i < dataNodes.getLength(); i++) {
      Node dataNode = dataNodes.item(i);
      if (dataNode.getNodeType() == Node.ELEMENT_NODE) {
        if (dataNode.getNodeName().equals(XmlTagConstants.ATTRIBUTES)) {
          attributes = (org.w3c.dom.Element) dataNode;
        }
      }
    }
    return attributes;
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());
    org.w3c.dom.Element attributes = getAttributes(data.getChildNodes());

    writeSupplier(relationships, element);
    writeClient(relationships, element);

    writeMultiplicity(attributes, getSupplierMultiplicity(element));
    writeMultiplicity(attributes, getClientMultiplicity(element));

    return data;
  }

  public void writeSupplier(org.w3c.dom.Element relationships, Element element) {
    Element supplier = getSupplier();

    if (supplier == null) {
      Logger.log(String.format("No supplier element found for relationship of type %s with id %s.",
          element.getHumanType(), MtipUtils.getId(element)));
      return;
    }

    org.w3c.dom.Element supplierTag =
        XmlWriter.createMtipRelationship(supplier, XmlTagConstants.SUPPLIER);
    XmlWriter.add(relationships, supplierTag);
  }

  public void writeClient(org.w3c.dom.Element relationships, Element element) {
    Element client = getClient();

    if (client == null) {
      Logger.log(String.format("No client element found for relationship of type %s with id %s.",
          element.getHumanType(), MtipUtils.getId(element)));
      return;
    }

    org.w3c.dom.Element clientTag =
        XmlWriter.createMtipRelationship(client, XmlTagConstants.CLIENT);
    XmlWriter.add(relationships, clientTag);
  }

  public void writeMultiplicity(org.w3c.dom.Element attributes, String multiplicity) {
    if (multiplicity == null || multiplicity.trim().isEmpty()) {
      return;
    }

    org.w3c.dom.Element multiplicityTag =
        XmlWriter.createMtipStringAttribute(XmlTagConstants.SUPPLIER_MULTIPLICITY, multiplicity);
    XmlWriter.add(attributes, multiplicityTag);
  }

  @CheckForNull
  public Element getSupplier() {
    return ModelHelper.getSupplierElement(element);
  }

  @CheckForNull
  public Element getClient() {
    return ModelHelper.getClientElement(element);
  }

  public String getSupplierMultiplicity(Element element) {
    return null;
  }

  public String getClientMultiplicity(Element element) {
    return null;
  }

  public void setSupplier(Element supplier) {
    ModelHelper.setSupplierElement(element, supplier);
  }

  public void setClient(Element client) {
    ModelHelper.setClientElement(element, client);
  }

  public static String getName(Element element) {
    if (element instanceof NamedElement) {
      return ((NamedElement) element).getName();
    }

    return element.getHumanName();
  }
}
