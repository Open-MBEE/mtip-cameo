package org.aero.mtip.metamodel.uaf.Parameters;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.InstanceSpecification;

public class ActualEnvironment extends InstanceSpecification {

	public ActualEnvironment(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.ACTUAL_ENVIRONMENT;
		this.xmlConstant = XmlTagConstants.ACTUAL_ENVIRONMENT;
	}
}
