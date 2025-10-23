package org.aero.mtip.metamodel.uaf.Operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.profile.Constraint;

public class OperationalConstraint extends Constraint {

	public OperationalConstraint(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.OPERATIONAL_CONSTRAINT;
		this.xmlConstant = XmlTagConstants.OPERATIONAL_CONSTRAINT;
	}
}
