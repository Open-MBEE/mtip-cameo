/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.activity;

import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.util.ElementData;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Signal;

public class SendSignalAction extends ActivityNode {

	public SendSignalAction(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.SEND_SIGNAL_ACTION;
		this.xmlConstant = XmlTagConstants.SEND_SIGNAL_ACTION;
		this.element = f.createSendSignalActionInstance();
		
		this.attributeReferences.add(XmlTagConstants.SIGNAL_TAG);
	}
	
	@Override
	public void addReferences() {
	  super.addReferences();
	  
	  setSignal();
	}
	
	private void setSignal() {
	  com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.SendSignalAction ssa = (com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.SendSignalAction)element;
      
      if (!elementData.hasAttribute(XmlTagConstants.SIGNAL_TAG)) {
        return;
      }
      
      Element signal = Importer.getInstance().getImportedElement(elementData.getAttribute(XmlTagConstants.SIGNAL_TAG));
      
      if (!(signal instanceof Signal)) {
        return;
      }
      
      ssa.setSignal((Signal)signal);
	}
	
	public Element createElement(Project project, Element owner, ElementData xmlElement) {
		super.createElement(project, owner, xmlElement);
		
		
		return element;
	}
	
	public org.w3c.dom.Element writeToXML(Element element) {
		org.w3c.dom.Element data = super.writeToXML(element);
		org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());
		
		writeSignal(relationships, element);
		
		return data;
	}
	
	private void writeSignal(org.w3c.dom.Element relationships, Element element) {
		com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.SendSignalAction ssa = (com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.SendSignalAction)element;
		Signal signal = ssa.getSignal();
		
		if(signal == null) {
			return;
		}
			
		org.w3c.dom.Element signalTag = XmlWriter.createMtipRelationship(signal, XmlTagConstants.SIGNAL_TAG);
		XmlWriter.add(relationships, signalTag);
	}
}
