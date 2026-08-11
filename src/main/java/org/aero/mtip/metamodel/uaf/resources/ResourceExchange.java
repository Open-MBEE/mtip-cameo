package org.aero.mtip.metamodel.uaf.resources;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.internalblock.InformationFlow;

public class ResourceExchange extends InformationFlow {

	public ResourceExchange(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.RESOURCE_EXCHANGE;
		this.xmlConstant = XmlTagConstants.RESOURCE_EXCHANGE;
	}
}