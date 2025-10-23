package org.aero.mtip.metamodel.uaf.Projects;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.Slot;

public class ActualProjectMilestoneRole extends Slot{
	
	public ActualProjectMilestoneRole(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.ACTUAL_PROJECT_MILESTONE_ROLE;
		this.xmlConstant = XmlTagConstants.ACTUAL_PROJECT_MILESTONE_ROLE;
	}
}
