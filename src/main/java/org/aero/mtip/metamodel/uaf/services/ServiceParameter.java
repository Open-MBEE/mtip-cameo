package org.aero.mtip.metamodel.uaf.services;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.activity.Parameter;

public class ServiceParameter extends Parameter {

	public ServiceParameter(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.SERVICE_PARAMETER;
		this.xmlConstant = XmlTagConstants.SERVICE_PARAMETER;
	}
}
