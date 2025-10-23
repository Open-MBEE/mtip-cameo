package org.aero.mtip.metamodel.uaf.services;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.Port;

public class ServicePort extends Port {

	public ServicePort(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.SERVICE_PORT;
		this.xmlConstant = XmlTagConstants.SERVICE_PORT;
	}
}
