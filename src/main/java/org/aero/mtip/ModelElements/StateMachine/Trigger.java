/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.ModelElements.StateMachine;

import java.util.HashMap;

import javax.annotation.CheckForNull;

import org.aero.mtip.ModelElements.CommonElement;
import org.aero.mtip.XML.Import.ImportXmlSysml;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.SysmlConstants;
import org.aero.mtip.util.XMLItem;
import org.aero.mtip.util.XmlTagConstants;
import org.w3c.dom.Document;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.AcceptEventAction;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Event;

public class Trigger extends CommonElement {
	public Trigger(String name, String EAID) {
		super(name, EAID);
		this.creationType = XmlTagConstants.ELEMENTSFACTORY;
		this.sysmlConstant = SysmlConstants.TRIGGER;
		this.xmlConstant = XmlTagConstants.TRIGGER;
		this.sysmlElement = f.createTriggerInstance();
	}

	@Override
	public Element createElement(Project project, Element owner, @CheckForNull XMLItem xmlElement) {
		com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger trigger = (com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger) sysmlElement;
		if(xmlElement != null) {
			if(xmlElement.hasAcceptEventAction()) {
				CameoUtils.logGUI("Setting accept event action of Trigger to AcceptEventAction with id: " + xmlElement.getNewAcceptEventAction());
				trigger.set_acceptEventActionOfTrigger((AcceptEventAction) project.getElementByID(xmlElement.getNewAcceptEventAction()));
			}

			if(xmlElement.hasElement(XmlTagConstants.EVENT_TAG)) {
				CameoUtils.logGUI("Setting Trigger Event to event with id: " + xmlElement.getAttribute(XmlTagConstants.EVENT_TAG));
				Event event = (Event) xmlElement.getElement(XmlTagConstants.EVENT_TAG);
				trigger.setEvent(event);
			} else {
				CameoUtils.logGUI("Trigger with id: " + EAID + " has no event.");
			}
			//Set transition of trigger if it has a transition
		}
		
		return sysmlElement;
	}
	
	@Override
	public void setOwner(Project project, Element owner) {
		com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger trigger = (com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger) sysmlElement;
		if(owner != null) {
			if(owner instanceof com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Transition) {
				trigger.set_transitionOfTrigger((com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Transition)owner);
			}
//			trigger.setOwner(owner);
		} else {
			trigger.setOwner(project.getPrimaryModel());
		}
	}
	
	@Override
	public void createDependentElements(Project project, HashMap<String, XMLItem> parsedXML, XMLItem modelElement) {
		CameoUtils.logGUI("Creating dependent elements for trigger...");
		if(modelElement.hasAcceptEventAction()) {	
			Element acceptEventAction = ImportXmlSysml.buildElement(project, parsedXML, parsedXML.get(modelElement.getAcceptEventAction()), modelElement.getAcceptEventAction());
			modelElement.setNewAcceptEventAction(acceptEventAction.getID());
		}
		if(modelElement.hasAttribute(XmlTagConstants.EVENT_TAG)) {
			String signal = modelElement.getAttribute(XmlTagConstants.EVENT_TAG);
			com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Event event = (com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.SignalEvent)ImportXmlSysml.getOrBuildElement(project, parsedXML, signal);
			modelElement.addElement(XmlTagConstants.EVENT_TAG, event);
			CameoUtils.logGUI("Event found and added to trigger XML.");
		} else {
			CameoUtils.logGUI("No event found in XML for trigger with id: " + EAID);
		}
	}

	@Override
	public org.w3c.dom.Element writeToXML(Element element, Project project, Document xmlDoc) {
		org.w3c.dom.Element data = super.writeToXML(element, project, xmlDoc);
		org.w3c.dom.Element attributes = getAttributes(data.getChildNodes());
		
		//Add reference to Event type of the trigger - since element is child of Trigger's parent's Activity, this must be explicitly written here
		com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger trigger = (com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger)element;
		Event event = trigger.getEvent();
		if(event != null) {
			org.w3c.dom.Element eventTag = createRel(xmlDoc, event, XmlTagConstants.EVENT_TAG);
			attributes.appendChild(eventTag);
		}
		
		AcceptEventAction aea = trigger.get_acceptEventActionOfTrigger();
		if(aea != null) {
			org.w3c.dom.Element aeaTag = xmlDoc.createElement("acceptEventAction");
			aeaTag.appendChild(xmlDoc.createTextNode(aea.getID()));
			attributes.appendChild(aeaTag);
		}
		
//		Event se = trigger.getEvent();
//		if(se != null) {
//			CameoUtils.logGUI("Trigger Event type is: " + se.getHumanType());
//			org.w3c.dom.Element signalEventTag = xmlDoc.createElement(XmlTagConstants.SIGNAL_EVENT_TAG);
//			signalEventTag.appendChild(xmlDoc.createTextNode(se.getID()));
//			attributes.appendChild(signalEventTag);
//		}
		
		return data;
	}
}
