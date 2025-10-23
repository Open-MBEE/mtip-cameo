package org.aero.mtip.metamodel.uaf.Operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.activity.ActivityParameterNode;

public class OperationalParameter extends ActivityParameterNode {

	public OperationalParameter(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.OPERATIONAL_PARAMETER;
		this.xmlConstant = XmlTagConstants.OPERATIONAL_PARAMETER;
	}
}
