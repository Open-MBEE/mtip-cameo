/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.activity;

import javax.annotation.CheckForNull;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdbasicbehaviors.Behavior;

public class Action extends ActivityNode {
    public static final String XML_TAG_BEHAVIOR = "behavior";
    
	public Action(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.ACTION;
		this.xmlConstant = XmlTagConstants.ACTION;
		this.element = f.createCallBehaviorActionInstance();
		
		this.attributeReferences.add(XML_TAG_BEHAVIOR);
	}
	
	@Override
	public void addReferences() {
	  setBehavior();
	}
	
	protected void setBehavior() {
	  if (!elementData.hasAttribute(XmlTagConstants.BEHAVIOR)) {
	    return;
	  }
	  
	  Element behavior =
	        Importer.getInstance().getImportedElement(elementData.getAttribute(XML_TAG_BEHAVIOR));

	    if (behavior == null) {
	      return;
	    }

	    getElementAsCallBehaviorAction().setBehavior((Behavior) behavior);
	}
	
	@Override
	public org.w3c.dom.Element writeToXML(Element element) {
		org.w3c.dom.Element data = super.writeToXML(element);
		org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());
		
		writeBehavior(relationships, element);
		
		return data;
	}
	
	public void writeBehavior(org.w3c.dom.Element relationships, Element element) {
		com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction action = (com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction)element;
		Behavior behavior = action.getBehavior();
		
		if(behavior == null) {
			return;
		}
		
		org.w3c.dom.Element behaviorTag = XmlWriter.createMtipRelationship(behavior, XML_TAG_BEHAVIOR);
		XmlWriter.add(relationships, behaviorTag);
	}
	
	@CheckForNull
	private com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction getElementAsCallBehaviorAction() {
	  if (!(element instanceof com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction)) {
	      return null; // ActivityParameterNodes are ActivityNodes but cannot be cast.
	    }

	    return (com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction) element;
	}
}
