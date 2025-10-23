package org.aero.mtip.metamodel.uaf.actualresources;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.InstanceSpecification;

public class ActualService extends InstanceSpecification {

	public ActualService(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.ACTUAL_SERVICE;
		this.xmlConstant = XmlTagConstants.ACTUAL_SERVICE;
	}
}
