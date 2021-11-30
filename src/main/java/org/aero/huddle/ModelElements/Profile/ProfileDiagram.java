package org.aero.huddle.ModelElements.Profile;

import org.aero.huddle.ModelElements.AbstractDiagram;
import org.aero.huddle.util.SysmlConstants;
import org.aero.huddle.util.XmlTagConstants;

//import com.nomagic.magicdraw.sysml.util.SysMLProfile;
import com.nomagic.magicdraw.uml.DiagramTypeConstants;

public class ProfileDiagram extends AbstractDiagram {

	public ProfileDiagram(String name, String EAID) {
		 super(name, EAID);
		 this.sysmlConstant = SysmlConstants.PROFILEDIAGRAM;
		 this.xmlConstant = XmlTagConstants.PROFILEDIAGRAM;
		 this.allowableElements = SysmlConstants.PROFILEDIAGRAM_TYPES;
	}
	
	@Override
	public String getSysmlConstant() {
		return 	DiagramTypeConstants.UML_PROFILE_DIAGRAM;
	}
	
	@Override
	public String getDiagramType() {
		return XmlTagConstants.PROFILEDIAGRAM;
	}
}
