/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */
package org.aero.mtip.metamodel.sysml.statemachine;

import java.util.Arrays;
import java.util.Collection;
import javax.annotation.CheckForNull;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.MtipUtils;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdbasicbehaviors.Behavior;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Region;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.StateMachine;

public class State extends CommonElement {
  public State(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.STATE;
    this.xmlConstant = XmlTagConstants.STATE;
    this.element = f.createStateInstance();

    this.attributeReferences.addAll(Arrays.asList(XmlTagConstants.SUBMACHINE, XmlTagConstants.DO_ACTIVITY, XmlTagConstants.ENTRY, XmlTagConstants.EXIT));
  }
  
  @Override
  public void addReferences() {
    super.addReferences();
    
    setDo();
    setEntry();
    setExit();
    setSubmachine();
  }
  
  private void setDo() {
    // TODO: Implement
  }
  
  private void setEntry() {
    // TODO: Implement
  }
  
  private void setExit() {
    // TODO: Implement
  }
  
  private void setSubmachine() {
    if (!elementData.hasAttribute(XmlTagConstants.SUBMACHINE)) {
      return;
    }
    
    Element submachine = Importer.getInstance().getImportedElement(elementData.getAttribute(XmlTagConstants.SUBMACHINE));
    
    if (submachine == null || !(submachine instanceof StateMachine)) {
      return;
    }
      
    getState().setSubmachine((StateMachine) submachine);
  }

  public void setOwner(Project project, Element owner) {
    if (owner == null) {
      Logger.log(String.format("Owner for state %s is null.", importId));
      return;
    }

    if (owner instanceof Region) {
      element.setOwner(owner);
      return;
    }

    if (owner instanceof StateMachine && setOwnerStateMachine(owner)) {
      return;
    }

    if (owner instanceof com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State && setOwnerState(owner)) {
      return;
    }

    owner = CameoUtils.findNearestRegion(project, owner);

    if (owner == null) {
      Logger.log(String.format("No valid parent found for state %s with id %s. State not imported.", name, element));
      ModelHelper.dispose(Arrays.asList(element));
      return;
    }

    element.setOwner(owner);
  }

  boolean setOwnerState(Element owner) {
    Region existingRegion = PseudoState.findExistingRegion(owner);

    if (existingRegion == null) {
      Logger.log(String.format("Creating new region for %s as child of %s for allowable owneship.", name, owner.getHumanName()));
      Region newRegion = f.createRegionInstance();
      newRegion.setOwner(owner);

      element.setOwner(newRegion);
      return true;
    }

    element.setOwner(existingRegion);
    return true;
  }

  boolean setOwnerStateMachine(Element owner) {
    Collection<Region> regions = ((StateMachine) owner).getRegion();

    if (regions == null) {
      return false;
    }

    Region region = regions.iterator().next();

    if (region == null) {
      return false;
    }

    element.setOwner(region);

    return true;
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    writeSubmachine(relationships, element);
    writeDoActivity(relationships, element);
    writeEntry(relationships, element);
    writeExit(relationships, element);

    return data;
  }

  @Override
  protected void writeParent(org.w3c.dom.Element relationships) {
    Element owner = element.getOwner();

    if (owner == null) {
      Logger.log(String.format("No parent found for state %s with id %s", element.getHumanName(), MtipUtils.getId(element)));
      return;
    }

    org.w3c.dom.Element hasParentTag = XmlWriter.createMtipRelationship(owner, XmlTagConstants.HAS_PARENT);
    XmlWriter.add(relationships, hasParentTag);
  }

  protected void writeSubmachine(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State state =
        (com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State) element;

    if (!state.isSubmachineState()) {
      return;
    }

    com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.StateMachine submachine = state.getSubmachine();

    if (submachine == null) {
      return;
    }

    org.w3c.dom.Element submachineTag = XmlWriter.createMtipRelationship(submachine, XmlTagConstants.SUBMACHINE);
    XmlWriter.add(relationships, submachineTag);
  }

  protected void writeDoActivity(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State state =
        (com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State) element;
    Behavior doActivity = state.getDoActivity();

    if (doActivity == null) {
      return;
    }

    org.w3c.dom.Element doActivityTag = XmlWriter.createMtipRelationship(doActivity, XmlTagConstants.DO_ACTIVITY);
    XmlWriter.add(relationships, doActivityTag);
  }

  protected void writeEntry(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State state =
        (com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State) element;
    Behavior entry = state.getEntry();

    if (entry == null) {
      return;
    }

    org.w3c.dom.Element entryTag = XmlWriter.createMtipRelationship(entry, XmlTagConstants.ENTRY);
    XmlWriter.add(relationships, entryTag);
  }

  protected void writeExit(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State state =
        (com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State) element;
    Behavior exit = state.getExit();

    if (exit == null) {
      return;
    }

    org.w3c.dom.Element exitTag = XmlWriter.createMtipRelationship(exit, XmlTagConstants.EXIT);
    XmlWriter.add(relationships, exitTag);
  }
  
  @CheckForNull
  private com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State getElementAsState() {
    if (!(element instanceof com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State)) {
      return null;
    }
    
    return (com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State) element;
  }
  
  public com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State getState() {
    return (com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State)element;
  }
}
