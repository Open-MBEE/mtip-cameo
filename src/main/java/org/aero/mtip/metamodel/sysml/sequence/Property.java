/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */
package org.aero.mtip.metamodel.sysml.sequence;

import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.XMLItem;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdinterfaces.Interface;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Association;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralBoolean;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.VisibilityKindEnum;

public class Property extends CommonElement {

  public Property(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.PROPERTY;
    this.xmlConstant = XmlTagConstants.PROPERTY;
    this.element = f.createPropertyInstance();
  }

  @Override
  public Element createElement(Project project, Element owner, XMLItem xmlElement) {
    super.createElement(project, owner, xmlElement);
    com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property property =
        (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) element;

    if (xmlElement.hasAttribute(XmlTagConstants.ATTRIBUTE_KEY_DEFAULT_VALUE)) {
      String defaultValue = xmlElement.getAttribute(XmlTagConstants.ATTRIBUTE_KEY_DEFAULT_VALUE);

      try {
        if (property.getType() != null) {
          if (xmlElement.getAttribute(XmlTagConstants.TYPED_BY)
              .contentEquals(SysmlConstants.BOOLEAN)) {
            boolean boolVal = Boolean.valueOf(defaultValue);
            LiteralBoolean valueSpecification = (LiteralBoolean) ModelHelper
                .createValueSpecification(project, property.getType(), boolVal, null);
            property.setDefaultValue(valueSpecification);
          } else if (xmlElement.getAttribute(XmlTagConstants.TYPED_BY)
              .contentEquals(SysmlConstants.INTEGER)) {
            int intVal = Integer.parseInt(defaultValue);
            ValueSpecification valueSpecification =
                ModelHelper.createValueSpecification(project, property.getType(), intVal, null);
            property.setDefaultValue(valueSpecification);
          } else if (xmlElement.getAttribute(XmlTagConstants.TYPED_BY)
              .contentEquals(SysmlConstants.REAL)) {
            double realVal = Double.parseDouble(defaultValue);
            LiteralReal valueSpecification = (LiteralReal) ModelHelper
                .createValueSpecification(project, property.getType(), realVal, null);
            valueSpecification.setValue(realVal);
            property.setDefaultValue(valueSpecification);
          } else if (xmlElement.getAttribute(XmlTagConstants.TYPED_BY)
              .contentEquals(SysmlConstants.STRING)) {
            LiteralString valueSpecification = (LiteralString) ModelHelper
                .createValueSpecification(project, property.getType(), defaultValue, null);
            property.setDefaultValue(valueSpecification);
          } else if (xmlElement.getAttribute(XmlTagConstants.TYPED_BY)
              .contentEquals(SysmlConstants.ELEMENT_VALUE)) {
            // ElementValue ev = ModelHelper.createValueSpecification(arg0, arg1, arg2, arg3)
          } else {
            CameoUtils.logGui("Primitive type not recognized: "
                + xmlElement.getAttribute(XmlTagConstants.TYPED_BY));
          }
        } else {
          CameoUtils.logGui("Property type is null. Cannot set default value.");
        }
      } catch (Exception exception) {
        Logger.log(String.format(
            "Error assigning default value to property with id: %s see stack trace:", importId));
        Logger.logException(exception);
      }
    }

    if (owner instanceof Interface) {
      ((NamedElement) element).setVisibility(VisibilityKindEnum.PUBLIC);
    }
    ((NamedElement) element).setVisibility(VisibilityKindEnum.PUBLIC);

    return element;
  }

  // Create Dependent Element default value if isElement()

  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element attributes = getAttributes(data.getChildNodes());
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    writeDefaultValue(attributes, element);
    writeAssociation(relationships, element);

    return data;
  }

  protected void writeDefaultValue(org.w3c.dom.Element attributes, Element element) {
    com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property property =
        (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) element;
    ValueSpecification vs = property.getDefaultValue();

    if (vs == null) {
      return;
    }

    org.w3c.dom.Element defaultValueTag = XmlWriter.createDefaultValueTag(vs);

    if (defaultValueTag == null) {
      return;
    }

    XmlWriter.add(attributes, defaultValueTag);

  }

  public void writeAssociation(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property property =
        (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) element;
    Association association = property.getAssociation();

    if (association == null) {
      return;
    }

    org.w3c.dom.Element associationTag =
        XmlWriter.createMtipRelationship(association, XmlTagConstants.ASSOCIATION_TAG);
    XmlWriter.add(relationships, associationTag);
  }
}
