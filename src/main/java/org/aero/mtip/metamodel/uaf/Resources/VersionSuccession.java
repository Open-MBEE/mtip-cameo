package org.aero.mtip.metamodel.uaf.Resources;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.general.Dependency;

public class VersionSuccession extends Dependency {

	public VersionSuccession(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.VERSION_SUCCESSION;
		this.xmlConstant = XmlTagConstants.VERSION_SUCCESSION;
	}
}
