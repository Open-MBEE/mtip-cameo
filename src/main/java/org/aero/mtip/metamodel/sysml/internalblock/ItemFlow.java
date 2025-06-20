/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.internalblock;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonDirectedRelationship;
import org.aero.mtip.profiles.SysML;

public class ItemFlow extends CommonDirectedRelationship {

	public ItemFlow(String name, String EAID) {
		super(name, EAID);
		creationType = XmlTagConstants.ELEMENTS_FACTORY;
		metamodelConstant = SysmlConstants.ITEM_FLOW;
		xmlConstant = XmlTagConstants.ITEMFLOW;
		element = f.createInformationFlowInstance();
		creationStereotype = SysML.getItemFlowStereotype();
	}
}
