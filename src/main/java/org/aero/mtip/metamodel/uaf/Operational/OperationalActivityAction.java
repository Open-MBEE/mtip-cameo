package org.aero.mtip.metamodel.uaf.Operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.activity.CallBehaviorAction;

public class OperationalActivityAction extends CallBehaviorAction {

	public OperationalActivityAction(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.OPERATIONAL_ACTIVITY_ACTION;
		this.xmlConstant = XmlTagConstants.OPERATIONAL_ACTIVITY_ACTION;
	}
}
