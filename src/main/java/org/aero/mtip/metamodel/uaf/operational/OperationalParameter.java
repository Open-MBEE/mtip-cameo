package org.aero.mtip.metamodel.uaf.operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.activity.Parameter;

public class OperationalParameter extends Parameter {

	public OperationalParameter(String name, String importId) {
		super(name, importId);
		
		metamodelConstant = UAFConstants.OPERATIONAL_PARAMETER;
		xmlConstant = XmlTagConstants.OPERATIONAL_PARAMETER;
	}
}
