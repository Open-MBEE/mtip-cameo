package org.aero.mtip.metamodel.uaf.Strategic;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.general.Abstraction;

public class Exhibits extends Abstraction {
	
	public Exhibits(String name, String importId) {
		super(name, importId);
		this.xmlConstant = XmlTagConstants.EXHIBITS;
		this.metamodelConstant = UAFConstants.EXHIBITS;
	}
}
