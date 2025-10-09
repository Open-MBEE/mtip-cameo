/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.requirements;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonRelationship;
import org.aero.mtip.profiles.SysML;

public class DeriveRequirement extends CommonRelationship {

	public DeriveRequirement(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.DERIVE_REQUIREMENT;
		this.xmlConstant = XmlTagConstants.DERIVEREQUIREMENT;
		this.element = f.createAbstractionInstance();
		this.creationStereotype = SysML.getDeriveRequirementStereotype();
	}
}
