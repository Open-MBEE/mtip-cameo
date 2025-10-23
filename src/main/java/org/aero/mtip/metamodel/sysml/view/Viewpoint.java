package org.aero.mtip.metamodel.sysml.view;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.profiles.SysML;

public class Viewpoint extends CommonElement {

	public Viewpoint(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.CLASS_WITH_STEREOTYPE;
		this.metamodelConstant = SysmlConstants.VIEWPOINT;
		this.xmlConstant = XmlTagConstants.VIEWPOINT;
		creationStereotype = SysML.getViewpointStereotype();
	}
}
