/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.profile;

import java.util.Arrays;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.Logger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

public class Constraint extends CommonElement {
  public static final String CONSTRAINED_ELEMENT = "constrainedElement";
  public static final String VALUE_SPECIFICATION = "valueSpecification";

  public Constraint(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.CONSTRAINT;
    this.xmlConstant = XmlTagConstants.CONSTRAINT;
    this.element = f.createConstraintInstance();

    this.attributeListDependencies.addAll(Arrays.asList(CONSTRAINED_ELEMENT, VALUE_SPECIFICATION));
  }

  @Override
  public void addReferences() {
    super.addReferences();

    // TOOD: Refactor into generic auxiliary resource ID checker/element finder. Ensure auxiliary
    // built-ins are found correclty.
    setConstrainedElement();
    setValueSpecification();
  }

  private void setConstrainedElement() {
    if (!elementData.hasListAttributes(CONSTRAINED_ELEMENT)) {
      return;
    }

    for (String constrainedElementImportId : elementData.getListAttributes(CONSTRAINED_ELEMENT)) {
      Element constrainedElement = Importer.getInstance().getImportedElement(constrainedElementImportId);

      if (constrainedElement == null) {
        continue;
      }

      getConstraint().getConstrainedElement().add(constrainedElement);
    }
  }

  private void setValueSpecification() {
    if (!elementData.hasAttribute(VALUE_SPECIFICATION)) {
      return;
    }

    Element valueSpecification = Importer.getInstance().getImportedElement(elementData.getAttribute(VALUE_SPECIFICATION));

    if (valueSpecification == null || !(valueSpecification instanceof ValueSpecification)) {
      Logger.log("ValueSpecification null when value specification exists in elementData.");
      return;
    }

    getConstraint().setSpecification((ValueSpecification) valueSpecification);
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());
    org.w3c.dom.Element attributes = getAttributes(data.getChildNodes());

    writeSpecification(attributes, element);
    writeRelationships(relationships, getConstraint().getConstrainedElement(), CONSTRAINED_ELEMENT);

    return data;
  }

  private void writeSpecification(org.w3c.dom.Element attributes, Element element) {
    com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint constraint =
        (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint) element;
    ValueSpecification vs = constraint.getSpecification();

    if (vs == null) {
      return;
    }

    org.w3c.dom.Element valueSpecTag = XmlWriter.createAttributeFromValueSpecification(vs, VALUE_SPECIFICATION);

    if (valueSpecTag == null) {
      return;
    }

    XmlWriter.add(attributes, valueSpecTag);
  }

  public com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint getConstraint() {
    return (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint) element;
  }
}
