/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */
package org.aero.mtip.metamodel.sysml.sequence;

import java.util.List;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.ElementData;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.uml.BaseElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.InteractionOperand;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.InteractionOperatorKind;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.InteractionOperatorKindEnum;

public class CombinedFragment extends CommonElement {
  public static final String INTERACTION_OPERANDS = "interactionOperands";
  public static final String INTERACTION_OPERATOR_KIND = "interacitonOperatorKind";

  public CombinedFragment(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.COMBINED_FRAGMENT;
    this.xmlConstant = XmlTagConstants.COMBINED_FRAGMENT;
    this.element = f.createCombinedFragmentInstance();

    this.attributeListDependencies.add(INTERACTION_OPERANDS);
  }

  @Override
  public void addReferences() {
    super.addReferences();

    setInteractionOperatorKind();
    setInteractionOperands();
  }

  private void setInteractionOperatorKind() {
    if (!elementData.hasAttribute(INTERACTION_OPERATOR_KIND)) {
      return;
    }
    
    String ioKind = elementData.getAttribute(INTERACTION_OPERATOR_KIND);
    
    if (ioKind == null || ioKind.trim().isEmpty()) {
      return;
    }
    
    InteractionOperatorKind kindEnum = InteractionOperatorKindEnum.get(ioKind);
    
    if (kindEnum == null) {
      return;
    }
    
    getCombinedFragment().setInteractionOperator(kindEnum);
  }

  private void setInteractionOperands() {
    if (!elementData.hasListAttributes(INTERACTION_OPERANDS)) {
      return;
    }
    
    List<String> interactionOperands = elementData.getListAttributes(INTERACTION_OPERANDS);
    
    for (String interactionOperandId : interactionOperands) {
      Element interactionOperand = Importer.getInstance().getImportedElement(interactionOperandId);
      
      if (interactionOperand == null || !(interactionOperand instanceof InteractionOperand)) {
        continue;
      }
      
      getCombinedFragment().getOperand().add((InteractionOperand)interactionOperand);
    }
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element attributes = getAttributes(data.getChildNodes());
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    writeOperatorKind(attributes, element);
    writeRelationships(relationships, getCombinedFragment().getOperand(), INTERACTION_OPERANDS);

    return data;
  }

  private void writeOperatorKind(org.w3c.dom.Element attributes, Element element) {
    com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.CombinedFragment combinedFragment =
        (com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.CombinedFragment) element;
    InteractionOperatorKind ioKind = combinedFragment.getInteractionOperator();

    if (ioKind == null) {
      return;
    }

    org.w3c.dom.Element ioKindTag = XmlWriter.createMtipStringAttribute(INTERACTION_OPERATOR_KIND, ioKind.toString());
    XmlWriter.add(attributes, ioKindTag);
  }

  public com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.CombinedFragment getCombinedFragment() {
    return (com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.CombinedFragment) element;
  }
}
