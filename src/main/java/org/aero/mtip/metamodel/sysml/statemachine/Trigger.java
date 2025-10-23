/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.statemachine;

import java.util.Arrays;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.ElementData;
import org.aero.mtip.util.MtipUtils;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.AcceptEventAction;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Event;

public class Trigger extends CommonElement {
  public static final String ACCEPT_EVENT_ACTION = "acceptEventAction";
  public static final String EVENT = "event";
  
  public Trigger(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.TRIGGER;
    this.xmlConstant = XmlTagConstants.TRIGGER;
    this.element = f.createTriggerInstance();
    
    this.attributeReferences.addAll(Arrays.asList(ACCEPT_EVENT_ACTION, EVENT));
  }
  
  @Override
  public void addReferences() {
    super.addReferences();
    
    setAcceptEventAction();
    setEvent();
  }
  
  private void setAcceptEventAction() {
    if (!elementData.hasAttribute(ACCEPT_EVENT_ACTION)) {
      return;
    }
    
    Element aea = Importer.getInstance().getImportedElement(elementData.getAttribute(ACCEPT_EVENT_ACTION));
    
    if (aea == null) {
      return;
    }
    
    getTrigger().set_acceptEventActionOfTrigger((AcceptEventAction) aea);
  }
  
  private void setEvent() {
    if (!elementData.hasAttribute(EVENT)) {
      return;
    }
    
    Element event = Importer.getInstance().getImportedElement(elementData.getAttribute(EVENT));
    
    if (event == null) {
      return;
    }
    
    getTrigger().setEvent((Event) event);
  }

  @Override
  public void setOwner(Element owner) {
    if (owner == null) {
      return;
    }

    if (owner instanceof com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Transition) {
      com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger trigger =
          (com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger) element;
      trigger.set_transitionOfTrigger((com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Transition) owner);
    }

    element.setOwner(owner);
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    // Add reference to Event type of the trigger - since element is child of Trigger's parent's
    // Activity, this must be explicitly written here
    writeAcceptEventAction(relationships, element);
    writeEvent(relationships, element);

    return data;
  }

  protected void writeEvent(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger trigger =
        (com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger) element;
    Event event = trigger.getEvent();

    if (event == null) {
      return;
    }

    org.w3c.dom.Element eventTag = XmlWriter.createMtipRelationship(event, EVENT);
    XmlWriter.add(relationships, eventTag);
  }

  protected void writeAcceptEventAction(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger trigger =
        (com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger) element;
    AcceptEventAction aea = trigger.get_acceptEventActionOfTrigger();

    if (aea == null) {
      return;
    }

    org.w3c.dom.Element aeaTag = XmlWriter.createMtipRelationship(aea, ACCEPT_EVENT_ACTION);
    XmlWriter.add(relationships, aeaTag);
  }
  
  public com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger getTrigger() {
    return (com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger)element;
  }
}
