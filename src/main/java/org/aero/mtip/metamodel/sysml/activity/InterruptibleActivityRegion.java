/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.activity;


import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;

public class InterruptibleActivityRegion extends CommonElement {
	public InterruptibleActivityRegion(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.INTERRUPTIBLE_ACTIVITY_REGION;
		this.xmlConstant = XmlTagConstants.INTERRUPTIBLE_ACTIVITY_REGION;
		this.element = f.createInterruptibleActivityRegionInstance();
	}
}
