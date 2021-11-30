package org.aero.huddle.ModelElements.Sequence;

import org.aero.huddle.ModelElements.CommonElement;
import org.aero.huddle.util.SysmlConstants;
import org.aero.huddle.util.XmlTagConstants;

public class MessageOccurrenceSpecification extends CommonElement {

	public MessageOccurrenceSpecification(String name, String EAID) {
		super(name, EAID);
		this.creationType = XmlTagConstants.ELEMENTSFACTORY;
		this.sysmlConstant = SysmlConstants.MESSAGEOCCURRENCESPECIFICATION;
		this.xmlConstant = XmlTagConstants.MESSAGEOCCURRENCESPECIFICATION;
		this.sysmlElement = f.createMessageOccurrenceSpecificationInstance();
	}
	
//	@Override
//	public void setOwner(Project project, Element owner) {
//		
//	}
}