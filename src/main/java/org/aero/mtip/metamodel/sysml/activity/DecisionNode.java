/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.activity;

import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdbasicbehaviors.Behavior;

public class DecisionNode extends ActivityNode {

  public DecisionNode(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.DECISION_NODE;
    this.xmlConstant = XmlTagConstants.DECISION_NODE;
    this.element = f.createDecisionNodeInstance();

    this.attributeReferences.add(XmlTagConstants.ATTRIBUTE_NAME_DECISION_INPUT);
  }

  @Override
  public void addReferences() {
    setDecisionInput();
  }

  public void setDecisionInput() {
    if (!elementData.hasAttribute(XmlTagConstants.ATTRIBUTE_NAME_DECISION_INPUT)) {
      return;
    }

    Element decisionInput =
        Importer.getInstance().getImportedElement(elementData.getAttribute(XmlTagConstants.ATTRIBUTE_NAME_DECISION_INPUT));

    if (!(decisionInput instanceof Behavior)) {
      return;
    }

    com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.DecisionNode decisionNode =
        (com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.DecisionNode) element;
    decisionNode.setDecisionInput((Behavior) decisionInput);
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    writeDecisionInput(relationships, element);

    return data;
  }

  public void writeDecisionInput(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.DecisionNode decisionNode =
        (com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.DecisionNode) element;
    Behavior decisionInput = decisionNode.getDecisionInput();

    if (decisionInput == null) {
      return;
    }

    org.w3c.dom.Element decisionInputTag = XmlWriter.createMtipRelationship(decisionInput, XmlTagConstants.ATTRIBUTE_NAME_DECISION_INPUT);
    XmlWriter.add(relationships, decisionInputTag);
  }
}
