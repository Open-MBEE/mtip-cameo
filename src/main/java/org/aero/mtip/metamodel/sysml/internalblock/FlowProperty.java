/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */
package org.aero.mtip.metamodel.sysml.internalblock;

import java.util.ArrayList;
import java.util.List;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.profiles.SysML;
import org.aero.mtip.util.ElementData;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class FlowProperty extends CommonElement {

	public FlowProperty(String name, String importId) {
		super(name, importId);
		creationType = XmlTagConstants.ELEMENTS_FACTORY;
		metamodelConstant = SysmlConstants.FLOW_PROPERTY;
		xmlConstant = XmlTagConstants.FLOW_PROPERTY;
		element = f.createPropertyInstance();
		creationStereotype = SysML.getFlowPropertyStereotype();
	}
	
	public Element createElement(Project project, Element owner, ElementData xmlElement) {
		super.createElement(project, owner, xmlElement);
		
//		TODO Find if flowproperty direction property exists in 2022x

//		if(xmlElement.hasAttribute(XmlTagConstants.ATTRIBUTE_KEY_DIRECTION)) {
//			StereotypesHelper.setStereotypePropertyValue(
//					element, 
//					SysML.getFlowPropertyStereotype(), 
//					SysMLProfile.FLOWPROPERTY_DIRECTION_PROPERTY, 
//					xmlElement.getAttribute(XmlTagConstants.ATTRIBUTE_KEY_DIRECTION));
//		}
		
		return element;
	}
	
	public org.w3c.dom.Element writeToXML(Element element) {
		org.w3c.dom.Element data = super.writeToXML(element);
		org.w3c.dom.Element attributes = getAttributes(data.getChildNodes());
		
		writeDirection(attributes, element);
		
		return data;
	}
	
	private void writeDirection(org.w3c.dom.Element attributes, Element element) {
//		TODO: Find if flowproperty direction exists in 2022x
//		Property property = (Property)element;
	    List<String> directionList = new ArrayList<String>();
//      StereotypesHelper.getStereotypePropertyValueAsString((Element) property, SysML.getFlowPropertyStereotype(), SysMLProfile.FLOWPROPERTY_DIRECTION_PROPERTY, false);
		
	    if(directionList.isEmpty()) {
	    	return;
	    }
	    
	    String direction = directionList.get(0);
	    
	    if(direction == null || direction.trim().isEmpty()) {
	    	return;
	    }
	    
    	org.w3c.dom.Element directionTag = XmlWriter.createMtipStringAttribute(XmlTagConstants.ATTRIBUTE_KEY_DIRECTION, direction);
    	XmlWriter.add(attributes, directionTag);
	}
}
