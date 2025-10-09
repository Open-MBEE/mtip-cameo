package org.aero.mtip.metamodel.uaf.Resources;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.Signal;

public class ResourceSignal extends Signal {

	public ResourceSignal(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.RESOURCE_SIGNAL;
		this.xmlConstant = XmlTagConstants.RESOURCE_SIGNAL;
	}
}
