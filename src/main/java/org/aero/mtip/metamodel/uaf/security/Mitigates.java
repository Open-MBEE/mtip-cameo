package org.aero.mtip.metamodel.uaf.security;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.general.Dependency;

public class Mitigates extends Dependency {

	public Mitigates(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.MITIGATES;
		this.xmlConstant = XmlTagConstants.MITIGATES;
	}
}
