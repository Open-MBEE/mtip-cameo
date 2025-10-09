/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.activity;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;

public class ActivityFinalNode extends ActivityNode {

	public ActivityFinalNode(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.ACTIVITY_FINAL_NODE;
		this.xmlConstant = XmlTagConstants.ACTIVITY_FINAL_NODE;
		this.element = f.createActivityFinalNodeInstance();
	}
}
