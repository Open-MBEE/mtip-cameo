/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.requirements;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonDirectedRelationship;
import org.aero.mtip.util.ElementData;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class Refine extends CommonDirectedRelationship {

	public Refine(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.REFINE;
		this.xmlConstant = XmlTagConstants.REFINE;
		this.element = f.createAbstractionInstance();
	}

	@Override
	public Element createElement(Project project, Element owner, Element client, Element supplier, ElementData xmlElement) {
		super.createElement(project, owner, client, supplier, xmlElement);
		
		Profile sysml = StereotypesHelper.getProfile(project, "SysML");
		Stereotype refineStereotype = StereotypesHelper.getStereotype(project,  "Refine", sysml);
		StereotypesHelper.addStereotype(element, refineStereotype);

		return element;
	}
}
