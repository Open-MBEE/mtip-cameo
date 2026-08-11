package org.aero.mtip.metamodel.uaf.resources;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.profile.Constraint;

public class ResourceConstraint extends Constraint {
	public ResourceConstraint(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.RESOURCE_CONSTRAINT;
		this.xmlConstant = XmlTagConstants.RESOURCE_CONSTRAINT;
	}
}