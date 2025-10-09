package org.aero.mtip.metamodel.uaf.security;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;

public class SecurityEnclave extends CommonElement {

	public SecurityEnclave(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.CLASS_WITH_STEREOTYPE;
		this.metamodelConstant = UAFConstants.SECURITY_ENCLAVE;
		this.xmlConstant = XmlTagConstants.SECURITY_ENCLAVE;
	}
}
