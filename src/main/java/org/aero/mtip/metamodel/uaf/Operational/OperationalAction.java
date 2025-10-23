package org.aero.mtip.metamodel.uaf.Operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.activity.CallBehaviorAction;

public class OperationalAction extends CallBehaviorAction {
	public OperationalAction(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.OPERATIONAL_ACTION;
		this.xmlConstant = XmlTagConstants.OPERATIONAL_ACTION;
	}
}
