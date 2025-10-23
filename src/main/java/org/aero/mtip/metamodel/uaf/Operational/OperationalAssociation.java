package org.aero.mtip.metamodel.uaf.Operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.general.Association;

public class OperationalAssociation extends Association {

	public OperationalAssociation(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.OPERATIONAL_ASSOCIATION;
		this.xmlConstant = XmlTagConstants.OPERATIONAL_ASSOCIATION;
	}
}
