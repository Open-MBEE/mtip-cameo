package org.aero.mtip.metamodel.uaf.services;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.internalblock.Connector;

public class ServiceConnector extends Connector {

	public ServiceConnector(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.SERVICE_CONNECTOR;
		this.xmlConstant = XmlTagConstants.SERVICE_CONNECTOR;
	}

}
