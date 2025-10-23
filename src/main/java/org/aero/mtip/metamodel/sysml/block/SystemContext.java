/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.block;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.profiles.SysML;

public class SystemContext extends CommonElement {

	public SystemContext(String name, String importId) {
		super(name, importId);
		creationType = XmlTagConstants.CLASS_WITH_STEREOTYPE;
		metamodelConstant = SysmlConstants.SYSTEM_CONTEXT;
		xmlConstant = XmlTagConstants.BLOCK;
		creationStereotype = SysML.getSystemContextStereotype();
	}
}
