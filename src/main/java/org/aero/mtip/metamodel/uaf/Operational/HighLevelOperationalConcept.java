package org.aero.mtip.metamodel.uaf.Operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.metamodel.uaf.UAFElement;

public class HighLevelOperationalConcept extends CommonElement implements UAFElement {
	
	public HighLevelOperationalConcept(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.CLASS_WITH_STEREOTYPE;
		this.metamodelConstant = UAFConstants.HIGH_LEVEL_OPERATIONAL_CONCEPT;
		this.xmlConstant = XmlTagConstants.HIGH_LEVEL_OPERATIONAL_CONCEPT;
	}
}
