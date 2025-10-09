package org.aero.mtip.metamodel.uaf.security;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.profile.Constraint;

public class SecurityConstraint extends Constraint {

	public SecurityConstraint(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.SECURITY_CONSTRAINT;
		this.xmlConstant = XmlTagConstants.SECURITY_CONSTRAINT;
	}

}
