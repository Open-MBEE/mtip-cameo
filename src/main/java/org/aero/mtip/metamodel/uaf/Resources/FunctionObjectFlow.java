package org.aero.mtip.metamodel.uaf.Resources;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.activity.ObjectFlow;

public class FunctionObjectFlow extends ObjectFlow {
	
	public FunctionObjectFlow(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.FUNCTION_OBJECT_FLOW;
		this.xmlConstant = XmlTagConstants.FUNCTION_OBJECT_FLOW;
	}
}
