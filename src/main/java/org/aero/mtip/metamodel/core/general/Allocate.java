/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.core.general;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonRelationship;
import org.aero.mtip.profiles.SysML;

public class Allocate extends CommonRelationship {
	public Allocate(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = SysmlConstants.ALLOCATE;
		this.xmlConstant = XmlTagConstants.ALLOCATE;
		this.element = f.createAbstractionInstance();
		this.creationStereotype = SysML.getAllocateStereotype();
	}
}
