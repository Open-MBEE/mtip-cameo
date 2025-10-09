package org.aero.mtip.metamodel.uaf.Resources;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.Operation;

public class ResourceMethod extends Operation {

	public ResourceMethod(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.RESOURCE_METHOD;
		this.xmlConstant = XmlTagConstants.RESOURCE_METHOD;
	}
}
