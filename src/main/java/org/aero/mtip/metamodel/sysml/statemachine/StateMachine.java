/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.statemachine;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.ElementData;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State;

public class StateMachine extends CommonElement {
  public static final String SUBMACHINE = "submachine";
  
  public StateMachine(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.STATE_MACHINE;
    this.xmlConstant = XmlTagConstants.STATEMACHINE;
    this.element = f.createStateMachineInstance();

    this.attributeListReferences.add(SUBMACHINE);
  }

  public Element createElement(Project project, Element owner, ElementData xmlElement) {
    super.createElement(project, owner, xmlElement);

    // Remove auto-created region as they are defined explicitly in the XML
    com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.StateMachine stateMachine =
        ((com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.StateMachine) element);
    stateMachine.getRegion().clear();

    return element;
  }
  
  @Override
  public void addReferences() {
    super.addReferences();
    
    setSubmachine();
  }
  
  private void setSubmachine() {
    if (!elementData.hasListAttributes(SUBMACHINE)) {
      return;
    }
    
    for (String submachineImportId : elementData.getListAttributes(SUBMACHINE)) {
      Element submachine = Importer.getInstance().getImportedElement(submachineImportId);
      
      if (submachine == null || !(submachine instanceof com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.StateMachine)) {
        continue;
      }
      
      getStateMachine().getSubmachineState().add((State)submachine);
    }
  }
  
  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());
    
    writeRelationships(relationships, getStateMachine().getSubmachineState() , SUBMACHINE);
    
    return data;
  }

  public com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.StateMachine getStateMachine() {
    return (com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.StateMachine)element;
  }
}
