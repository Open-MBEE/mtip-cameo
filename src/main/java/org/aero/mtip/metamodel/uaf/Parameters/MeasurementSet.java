                 package org.aero.mtip.metamodel.uaf.Parameters;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.profile.DataType;

public class MeasurementSet extends DataType {
	public MeasurementSet (String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.MEASUREMENT_SET;
		this.xmlConstant = XmlTagConstants.MEASUREMENT_SET;
	}
}
