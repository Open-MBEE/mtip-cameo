package org.aero.huddle.ModelElements.Requirements;

import org.aero.huddle.ModelElements.CommonDirectedRelationship;
import org.aero.huddle.util.SysmlConstants;
import org.aero.huddle.util.XMLItem;
import org.aero.huddle.util.XmlTagConstants;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class Verify extends CommonDirectedRelationship {

	public Verify(String name, String EAID) {
		super(name, EAID);
		this.creationType = XmlTagConstants.ELEMENTSFACTORY;
		this.sysmlConstant = SysmlConstants.VERIFY;
		this.xmlConstant = XmlTagConstants.VERIFY;
		this.sysmlElement = f.createAbstractionInstance();
	}

	@Override
	public Element createElement(Project project, Element owner, Element client, Element supplier, XMLItem xmlElement) {
		super.createElement(project, owner, client, supplier, xmlElement);
		
		Profile sysml = StereotypesHelper.getProfile(project, "SysML");
		Stereotype verifyStereotype = StereotypesHelper.getStereotype(project,  "Verify", sysml);
		StereotypesHelper.addStereotype(sysmlElement, verifyStereotype);
		
		return sysmlElement;
	}
}
