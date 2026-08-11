package org.aero.mtip.metamodel.uaf.resources;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.uaf.Parameter;

public class ResourceParameter extends Parameter {

	public ResourceParameter(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.RESOURCE_PARAMETER;
		this.xmlConstant = XmlTagConstants.RESOURCE_PARAMETER;
	}
}