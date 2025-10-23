/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.activity;

import javax.annotation.CheckForNull;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Operation;

public class CallOperationAction extends ActivityNode {

  public CallOperationAction(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.CALL_OPERATION_ACTION;
    this.xmlConstant = XmlTagConstants.CALL_OPERATION_ACTION;
    this.element = f.createCallOperationActionInstance();

    this.attributeReferences.add(XmlTagConstants.OPERATION_TAG);
  }

  @Override
  public void addReferences() {
    setOperation();
  }

  private void setOperation() {
    if (!elementData.hasAttribute(XmlTagConstants.OPERATION_TAG)) {
      return;
    }

    Element operation = Importer.getInstance().getImportedElement(elementData.getAttribute(XmlTagConstants.OPERATION_TAG));

    if (operation == null) {
      return;
    }

    getElementAsCallOperationAction().setOperation((Operation) operation);
  }


  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    writeOperation(relationships, element);

    return data;
  }

  public void writeOperation(org.w3c.dom.Element relationships, Element element) {
    Operation operation = ((com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallOperationAction) element).getOperation();

    if (operation == null) {
      return;
    }

    org.w3c.dom.Element operationTag = XmlWriter.createMtipRelationship(operation, XmlTagConstants.OPERATION_TAG);
    XmlWriter.add(relationships, operationTag);
  }

  @CheckForNull
  public com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallOperationAction getElementAsCallOperationAction() {
    if (!(element instanceof com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallOperationAction)) {
      return null;
    }

    return (com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallOperationAction) element;
  }
}
