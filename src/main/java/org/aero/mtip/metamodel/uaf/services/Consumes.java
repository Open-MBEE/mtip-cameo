package org.aero.mtip.metamodel.uaf.services;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.general.Abstraction;

public class Consumes extends Abstraction {

	public Consumes(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.CONSUMES;
		this.xmlConstant = XmlTagConstants.CONSUMES;
	}
}
