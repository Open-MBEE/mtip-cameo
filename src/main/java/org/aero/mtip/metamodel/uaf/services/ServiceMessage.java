package org.aero.mtip.metamodel.uaf.services;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.sequence.Message;

public class ServiceMessage extends Message {

	public ServiceMessage(String name, String EAID) {
		super(name, EAID);
		this.metamodelConstant = UAFConstants.SERVICE_MESSAGE;
		this.xmlConstant = XmlTagConstants.SERVICE_MESSAGE;
	}
}
