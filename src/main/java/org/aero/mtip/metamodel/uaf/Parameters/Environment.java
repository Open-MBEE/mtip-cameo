package org.aero.mtip.metamodel.uaf.Parameters;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.profile.DataType;

public class Environment extends DataType {
	public Environment (String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.ENVIRONMENT;
		this.xmlConstant = XmlTagConstants.ENVIRONMENT;
	}
}
