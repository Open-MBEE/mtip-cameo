/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.sequence;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;

public class MessageOccurrenceSpecification extends CommonElement {

	public MessageOccurrenceSpecification(String name, String EAID) {
		super(name, EAID);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.MESSAGE_OCCURRENCE_SPECIFICATION;
		this.xmlConstant = XmlTagConstants.MESSAGE_OCCURRENCE_SPECIFICATION;
		this.element = f.createMessageOccurrenceSpecificationInstance();
	}
	
//	@Override
//	public void setOwner(Project project, Element owner) {
//		
//	}
}
