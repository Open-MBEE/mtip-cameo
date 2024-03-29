/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.ModelElements.Activity;

import java.util.HashMap;

import org.aero.mtip.XML.Import.ImportXmlSysml;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.SysmlConstants;
import org.aero.mtip.util.XMLItem;
import org.aero.mtip.util.XmlTagConstants;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class OutputPin extends ActivityNode {

	public OutputPin(String name, String EAID) {
		super(name, EAID);
		this.creationType = XmlTagConstants.ELEMENTSFACTORY;
		this.sysmlConstant = SysmlConstants.OUTPUTPIN;
		this.xmlConstant = XmlTagConstants.OUTPUTPIN;
		this.sysmlElement = f.createOutputPinInstance();
	}
	
	@Override
	public Element createElement(Project project, Element owner, XMLItem xmlElement) {
		Element element = super.createElement(project, owner, xmlElement);
		
		com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OutputPin outputPin = (com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OutputPin)element;
		
		if(xmlElement.hasAttribute(XmlTagConstants.SYNC_ELEMENT)) {
			element.setSyncElement((Element) project.getElementByID(xmlElement.getAttribute(ImportXmlSysml.idConversion(XmlTagConstants.SYNC_ELEMENT))));
		}
		return outputPin;
	}
	
	@Override
	public void createDependentElements(Project project, HashMap<String, XMLItem> parsedXML, XMLItem modelElement) {
		super.createDependentElements(project, parsedXML, modelElement);
		if(modelElement.hasAttribute(XmlTagConstants.SYNC_ELEMENT)) {
			String syncElementId = modelElement.getAttribute(XmlTagConstants.SYNC_ELEMENT);
			ImportXmlSysml.buildElement(project, parsedXML, parsedXML.get(syncElementId), syncElementId);
		}
	}
	
	@Override
	public void setOwner(Project project, Element owner) {
		if(!(owner instanceof com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.Action)) {
			owner = CameoUtils.findNearestActivity(project, owner);
		}
		sysmlElement.setOwner(owner);
	}
}
