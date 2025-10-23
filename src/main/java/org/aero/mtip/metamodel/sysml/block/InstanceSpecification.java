/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */
package org.aero.mtip.metamodel.sysml.block;

import java.util.stream.Collectors;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.apache.commons.lang3.ArrayUtils;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Classifier;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class InstanceSpecification extends CommonElement {
  public InstanceSpecification(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.INSTANCE_SPECIFICATION;
    this.xmlConstant = XmlTagConstants.INSTANCESPECIFICATION;
    this.element = f.createInstanceSpecificationInstance();

    this.attributeListReferences.add(XmlTagConstants.CLASSIFIED_BY);
  }

  @Override
  public void addReferences() {
    super.addReferences();

    setInstanceClassifer();
  }


  protected void setInstanceClassifer() {
    if (!elementData.hasAttribute(XmlTagConstants.CLASSIFIED_BY)) {
      return;
    }

    Element classifier = Importer.getInstance().buildEntity(importId);

    if (classifier == null) {
      classifier = (Element) project.getElementByID(elementData.getAttribute(XmlTagConstants.CLASSIFIED_BY));
    }

    if (classifier == null) {
      return;
    }

    if (!(classifier instanceof Classifier)) {
      return;
    }

    ModelHelper.setClassifierForInstanceSpecification((Classifier) classifier, getInstanceSpecification(), true);
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    writeRelationships(relationships,
        getInstanceSpecification().getClassifier().stream()
            .filter(x -> !ArrayUtils.contains(SysmlConstants.defaultClassifiers, x.getName())).collect(Collectors.toList()),
        XmlTagConstants.CLASSIFIED_BY);

    return data;
  }

  public com.nomagic.uml2.ext.magicdraw.classes.mdkernel.InstanceSpecification getInstanceSpecification() {
    return (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.InstanceSpecification) element;
  }
}
