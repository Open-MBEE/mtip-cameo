/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.activity;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;

public class CreateObjectAction extends ActivityNode {

	public CreateObjectAction(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.CREATE_OBJECT_ACTION;
		this.xmlConstant = XmlTagConstants.CREATE_OBJECT_ACTION;
		this.element = f.createCreateObjectActionInstance();
	}
}
