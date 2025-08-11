/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */
package org.aero.mtip.metamodel.sysml.internalblock;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonRelationship;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class InformationFlow extends CommonRelationship {

	public InformationFlow(String name, String EAID) {
		super(name, EAID);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.INFORMATION_FLOW;
		this.xmlConstant = XmlTagConstants.INFORMATIONFLOW;
		this.element = f.createInformationFlowInstance();
	}
	
	@Override
	public void setSupplier(Element supplier) {
		com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow informationFlow = (com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow)element;
		informationFlow.getSource().add(supplier);
	}
	
	@Override 
	public void setClient(Element client) {
		com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow informationFlow = (com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow)element;
		informationFlow.getTarget().add(client);
	}
	
	@Override
	public Element getSupplier() {
		com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow informationFlow = (com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow)element;
		return informationFlow.getSource().iterator().next();
	}
	
	@Override
	public Element getClient() {
		com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow informationFlow = (com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow)element;
		return informationFlow.getTarget().iterator().next();
	}
}
