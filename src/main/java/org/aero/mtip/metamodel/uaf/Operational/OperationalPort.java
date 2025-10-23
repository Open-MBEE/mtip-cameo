package org.aero.mtip.metamodel.uaf.Operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.Port;

public class OperationalPort extends Port {

	public OperationalPort(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.OPERATIONAL_PORT;
		this.xmlConstant = XmlTagConstants.OPERATIONAL_PORT;
	}
}
