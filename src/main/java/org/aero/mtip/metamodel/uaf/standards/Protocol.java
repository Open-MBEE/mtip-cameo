package org.aero.mtip.metamodel.uaf.standards;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.metamodel.uaf.UAFElement;

public class Protocol extends CommonElement implements UAFElement{
	
	public Protocol(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.CLASS_WITH_STEREOTYPE;
		this.metamodelConstant = UAFConstants.PROTOCOL;
		this.xmlConstant = XmlTagConstants.PROTOCOL;
	}

}
