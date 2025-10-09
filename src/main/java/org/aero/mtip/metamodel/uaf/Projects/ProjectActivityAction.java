package org.aero.mtip.metamodel.uaf.Projects;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.activity.CallBehaviorAction;

public class ProjectActivityAction extends CallBehaviorAction {
	
	public ProjectActivityAction(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.PROJECT_ACTIVITY_ACTION;
		this.xmlConstant = XmlTagConstants.PROJECT_ACTIVITY_ACTION;
	}
}