package org.aero.mtip.metamodel.uaf.security;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.general.Dependency;

public class Affects extends Dependency {

	public Affects(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.AFFECTS;
		this.xmlConstant = XmlTagConstants.AFFECTS;
	}

}
