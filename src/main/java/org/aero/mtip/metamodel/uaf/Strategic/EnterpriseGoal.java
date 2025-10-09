package org.aero.mtip.metamodel.uaf.Strategic;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.requirements.Requirement;
import org.aero.mtip.metamodel.uaf.UAFElement;

public class EnterpriseGoal extends Requirement implements UAFElement {
	
	public EnterpriseGoal(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.CLASS_WITH_STEREOTYPE;
		this.metamodelConstant = UAFConstants.ENTERPRISE_GOAL;
		this.xmlConstant = XmlTagConstants.ENTERPRISE_GOAL;
	}
}
