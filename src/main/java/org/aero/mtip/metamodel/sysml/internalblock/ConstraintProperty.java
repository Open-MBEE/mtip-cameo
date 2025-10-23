/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.internalblock;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.profiles.MDCustomizationForSysML;

public class ConstraintProperty extends CommonElement {

	public ConstraintProperty(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.xmlConstant = XmlTagConstants.CONSTRAINT_PROPERTY;
		this.metamodelConstant = SysmlConstants.CONSTRAINT_PROPERTY;
		this.element = f.createPropertyInstance();
		this.creationStereotype = MDCustomizationForSysML.getConstraintPropertyStereotype();
	}
}
