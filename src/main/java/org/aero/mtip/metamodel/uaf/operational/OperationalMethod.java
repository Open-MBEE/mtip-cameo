package org.aero.mtip.metamodel.uaf.operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.Operation;

public class OperationalMethod extends Operation {

	public OperationalMethod(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.OPERATIONAL_METHOD;
		this.xmlConstant = XmlTagConstants.OPERATIONAL_METHOD;
	}
}
