package org.aero.mtip.metamodel.uaf.security;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.general.Dependency;

public class Protects extends Dependency {

	public Protects(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.PROTECTS;
		this.xmlConstant = XmlTagConstants.PROTECTS;
	}
}
