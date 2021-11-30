package org.aero.huddle.ModelElements.StateMachine;

import org.aero.huddle.ModelElements.CommonElement;
import org.aero.huddle.util.SysmlConstants;
import org.aero.huddle.util.XmlTagConstants;

public class Region extends CommonElement {

	public Region(String name, String EAID) {
		super(name, EAID);
		this.creationType = XmlTagConstants.ELEMENTSFACTORY;
		this.sysmlConstant = SysmlConstants.REGION;
		this.xmlConstant = XmlTagConstants.REGION;
		this.sysmlElement = f.createRegionInstance();
	}

}