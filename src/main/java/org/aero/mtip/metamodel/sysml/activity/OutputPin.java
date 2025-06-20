/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.activity;

import java.util.HashMap;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.XMLItem;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class OutputPin extends CommonElement {

	public OutputPin(String name, String EAID) {
		super(name, EAID);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.OUTPUT_PIN;
		this.xmlConstant = XmlTagConstants.OUTPUT_PIN;
		this.element = f.createOutputPinInstance();
	}
	
	@Override
	public Element createElement(Project project, Element owner, XMLItem xmlElement) {
		Element element = super.createElement(project, owner, xmlElement);
		
		com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OutputPin outputPin = (com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OutputPin)element;
		
		if(xmlElement.hasAttribute(XmlTagConstants.SYNC_ELEMENT)) {
			element.setSyncElement((Element) project.getElementByID(xmlElement.getAttribute(Importer.idConversion(XmlTagConstants.SYNC_ELEMENT))));
		}
		return outputPin;
	}
	
	@Override
	public void createDependentElements(HashMap<String, XMLItem> parsedXML, XMLItem modelElement) {
		super.createDependentElements(parsedXML, modelElement);
		
		if(modelElement.hasAttribute(XmlTagConstants.SYNC_ELEMENT)) {
			String syncElementId = modelElement.getAttribute(XmlTagConstants.SYNC_ELEMENT);
			Importer.getInstance().buildElement(parsedXML, parsedXML.get(syncElementId));
		}
	}
	
	@Override
	public void setOwner(Element owner) {
		if(!(owner instanceof com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.Action)) {
			owner = CameoUtils.findNearestActivity(owner);
		}

		element.setOwner(owner);
	}
}
