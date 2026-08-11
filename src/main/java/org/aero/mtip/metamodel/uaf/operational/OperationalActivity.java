package org.aero.mtip.metamodel.uaf.operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.activity.Activity;

public class OperationalActivity extends Activity {

	public OperationalActivity(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.OPERATIONAL_ACTIVITY;
		this.xmlConstant = XmlTagConstants.OPERATIONAL_ACTIVITY;
	}
}
