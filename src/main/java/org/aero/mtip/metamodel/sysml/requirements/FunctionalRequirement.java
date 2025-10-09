/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.requirements;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.profiles.SysML;

public class FunctionalRequirement extends Requirement {

	public FunctionalRequirement(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.CLASS_WITH_STEREOTYPE;
		this.metamodelConstant = SysmlConstants.FUNCTIONAL_REQUIREMENT;
		this.xmlConstant = XmlTagConstants.FUNCTIONAL_REQUIREMENT;
		this.creationStereotype = SysML.getFunctionalRequirementStereotype();
	}
}
