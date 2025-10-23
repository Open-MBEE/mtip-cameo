package org.aero.mtip.metamodel.uaf.Projects;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.InstanceSpecification;

public class ActualProject extends InstanceSpecification {

	public ActualProject(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.ACTUAL_PROJECT;
		this.xmlConstant = XmlTagConstants.ACTUAL_PROJECT;
	}
}
