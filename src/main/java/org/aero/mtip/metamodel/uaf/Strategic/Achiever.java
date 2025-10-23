package org.aero.mtip.metamodel.uaf.Strategic;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.InstanceSpecification;

public class Achiever extends InstanceSpecification {

	public Achiever(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.ACHIEVER;
		this.xmlConstant = XmlTagConstants.ACHIEVER;
	}
}
