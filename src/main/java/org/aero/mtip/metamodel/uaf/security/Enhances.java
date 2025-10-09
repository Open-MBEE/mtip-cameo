package org.aero.mtip.metamodel.uaf.security;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.general.Abstraction;

public class Enhances extends Abstraction {

	public Enhances(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.ENHANCES;
		this.xmlConstant = XmlTagConstants.ENHANCES;
	}
}
