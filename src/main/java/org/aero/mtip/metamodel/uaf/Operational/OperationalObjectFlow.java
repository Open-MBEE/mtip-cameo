package org.aero.mtip.metamodel.uaf.Operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.activity.ObjectFlow;

public class OperationalObjectFlow extends ObjectFlow {

	public OperationalObjectFlow(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.OPERATIONAL_OBJECT_FLOW;
		this.xmlConstant = XmlTagConstants.OPERATIONAL_OBJECT_FLOW;
	}
}
