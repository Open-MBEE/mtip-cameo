/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.core.general;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.util.XMLItem;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.AggregationKindEnum;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Association;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class Composition extends org.aero.mtip.metamodel.core.general.Association {
	public Composition(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.COMPOSITION;
		this.xmlConstant = XmlTagConstants.COMPOSITION;
		this.element = f.createAssociationInstance();
	}

	@Override
	public Element createElement(Project project, Element owner, Element client, Element supplier, XMLItem xmlElement) {
		Association association = (Association) super.createElement(project, owner, client, supplier, xmlElement);
		
		//To create composition, the property of the end of the association relationship must be set to the composite aggregation enumeration
		if(association != null) {
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property firstMemberEnd = ModelHelper.getFirstMemberEnd(association);
			com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property secondMemberEnd = ModelHelper.getSecondMemberEnd(association);
			ModelHelper.setNavigable(firstMemberEnd, true);
			ModelHelper.setNavigable(secondMemberEnd, false);
			firstMemberEnd.setAggregation(AggregationKindEnum.COMPOSITE);
		}
		
		return association;
	}
}
