package org.aero.mtip.metamodel.uaf.Parameters;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.Slot;

public class ActualMeasurement extends Slot {
	public ActualMeasurement(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.ACTUAL_MEASUREMENT;
		this.xmlConstant = XmlTagConstants.ACTUAL_MEASUREMENT;
	}
}
