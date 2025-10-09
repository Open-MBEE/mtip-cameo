/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.internalblock;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.profiles.SysML;

public class BindingConnector extends Connector {

	public BindingConnector(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.xmlConstant = XmlTagConstants.BINDINGCONNECTOR;
		this.metamodelConstant = SysmlConstants.BINDING_CONNECTOR;
		this.element= f.createConnectorInstance();
		this.creationStereotype = SysML.getBindingConnectorStereotype();
	}
}
