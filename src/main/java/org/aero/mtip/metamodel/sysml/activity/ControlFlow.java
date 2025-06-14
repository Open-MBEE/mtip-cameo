/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.activity;

import java.util.Arrays;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonRelationship;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.MtipUtils;
import org.aero.mtip.util.XMLItem;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityEdge;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

public class ControlFlow extends CommonRelationship {

	public ControlFlow(String name, String EAID) {
		super(name, EAID);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.CONTROL_FLOW;
		this.xmlConstant = XmlTagConstants.CONTROLFLOW;
		this.element = f.createControlFlowInstance();
	}
	
	@Override
	public Element createElement(Project project, Element owner, Element client, Element supplier, XMLItem xmlElement) {
		super.createElement(project,owner, client, supplier, xmlElement);
		
		if(xmlElement.hasAttribute(XmlTagConstants.GUARD)) {
			setGuard(xmlElement);
		}
		
		return element;
	}
	
	private void setGuard(XMLItem xmlElement) {
		com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ControlFlow cf = (com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ControlFlow)element;
		ValueSpecification guard = cf.getGuard();
		
		if(guard != null) {
			ModelHelper.dispose(Arrays.asList(element));
		}
		
		LiteralString specification = f.createLiteralStringInstance();
		specification.setValue(xmlElement.getAttribute(XmlTagConstants.GUARD));			
		cf.setGuard(specification);
	}
	
	
	@Override
	public org.w3c.dom.Element writeToXML(Element element) {
		org.w3c.dom.Element data = super.writeToXML(element);
		org.w3c.dom.Element attributes = getAttributes(data.getChildNodes());
		
		writeGuard(attributes, element);
		
		return data;
	}
	
	public void writeGuard(org.w3c.dom.Element attributes, Element element) {
		com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ControlFlow cf = (com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ControlFlow)element;
		ValueSpecification vs = cf.getGuard();
		
		if(vs == null) {
			return;
		}
		
		org.w3c.dom.Element guardTag = XmlWriter.createAttributeFromValueSpecification(vs, XmlTagConstants.GUARD);
		
		if(guardTag == null) {
			Logger.log(String.format("Failed to create xml attribute for guard of control flow with id %s", MtipUtils.getId(element)));
			return;	
		}
		
		XmlWriter.add(attributes, guardTag);
	}
	
	@Override
	public void setOwner(Element owner) {
		if(!(owner instanceof Activity)) {
			owner = CameoUtils.findNearestActivity(supplier);
		}
		element.setOwner(owner);
	}
	
	@Override
	public void setSupplier() {
		ActivityEdge activityEdge = (ActivityEdge)element;
		activityEdge.setSource((ActivityNode) supplier);
	}
	
	@Override
	public void setClient() {
		ActivityEdge activityEdge = (ActivityEdge)element;
		activityEdge.setTarget((ActivityNode) client);
	}
	
	@Override
	public Element getSupplier(Element element) {
		ActivityEdge activityEdge = (ActivityEdge)element;
		return activityEdge.getSource();
	}
	
	@Override
	public Element getClient(Element element) {
		ActivityEdge activityEdge = (ActivityEdge)element;
		return activityEdge.getTarget();
	}
}
