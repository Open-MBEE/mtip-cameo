/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.requirements;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.profiles.SysML;

public class DesignConstraint extends Requirement {

	public DesignConstraint(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.CLASS_WITH_STEREOTYPE;
		this.metamodelConstant = SysmlConstants.DESIGN_CONSTRAINT;
		this.xmlConstant = XmlTagConstants.DESIGN_CONSTRAINT; 
		this.creationStereotype = SysML.getDesignConstraintStereotype();
	}
}
