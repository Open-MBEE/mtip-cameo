package org.aero.mtip.metamodel.uaf.Operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.Enumeration;

public class OperationalExchangeKind extends Enumeration{
	public OperationalExchangeKind(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.CLASS_WITH_STEREOTYPE;
		this.metamodelConstant = UAFConstants.OPERATIONAL_EXCHANGE_KIND;
		this.xmlConstant = XmlTagConstants.OPERATIONAL_EXCHANGE_KIND;
	}
}
