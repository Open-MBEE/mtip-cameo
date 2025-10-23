/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.core.matrix;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.util.ElementData;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class RefineRequirementMatrix extends AbstractMatrix {
	public RefineRequirementMatrix(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = SysmlConstants.REFINE_REQUIREMENT_MATRIX;
		this.xmlConstant = XmlTagConstants.REFINE_REQUIREMENT_MATRIX;
		this.cameoConstant = SysmlConstants.CAMEO_REFINE_REQUIREMENT_MATRIX;
	}
		
	@Override
	public Element createElement(Project project, Element owner, ElementData xmlElement) {
		return super.createElement(project, owner, xmlElement);
	}
}