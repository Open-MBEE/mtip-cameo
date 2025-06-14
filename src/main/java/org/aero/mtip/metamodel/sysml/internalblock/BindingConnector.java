/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.internalblock;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.util.XMLItem;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class BindingConnector extends Connector {

	public BindingConnector(String name, String EAID) {
		super(name, EAID);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.xmlConstant = XmlTagConstants.BINDINGCONNECTOR;
		this.metamodelConstant = SysmlConstants.BINDING_CONNECTOR;
		this.element= f.createConnectorInstance();
	}

	@Override
	public Element createElement(Project project, Element owner, Element client, Element supplier, XMLItem xmlElement) {
		super.createElement(project, owner, client, supplier, xmlElement);
		Profile sysmlProfile = StereotypesHelper.getProfile(project, "SysML"); 
		Stereotype bindingConnectorStereotype = StereotypesHelper.getStereotype(project, "BindingConnector", sysmlProfile);
		StereotypesHelper.addStereotype(element, bindingConnectorStereotype);
		
		return element;
	}
}
