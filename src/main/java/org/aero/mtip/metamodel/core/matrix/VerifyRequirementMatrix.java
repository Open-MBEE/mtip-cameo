/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.core.matrix;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.util.XMLItem;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class VerifyRequirementMatrix extends AbstractMatrix {
	public VerifyRequirementMatrix(String name, String EAID) {
		super(name, EAID);
		this.metamodelConstant = SysmlConstants.VERIFY_REQUIREMENT_MATRIX;
		this.xmlConstant = XmlTagConstants.VERIFY_REQUIREMENT_MATRIX;
		this.cameoConstant = SysmlConstants.CAMEO_VERIFY_REQUIREMENT_MATRIX;
	}
		
	@Override
	public Element createElement(Project project, Element owner, XMLItem xmlElement) {
		return super.createElement(project, owner, xmlElement);
	}
}
