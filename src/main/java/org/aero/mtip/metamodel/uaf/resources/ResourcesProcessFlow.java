package org.aero.mtip.metamodel.uaf.resources;

import org.aero.mtip.constants.CameoDiagramConstants;
import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.activity.ActivityDiagram;

public class ResourcesProcessFlow extends ActivityDiagram {
	
	public ResourcesProcessFlow(String name, String importId) {
		super(name, importId);
		
		metamodelConstant = UAFConstants.RESOURCES_PROCESS_FLOW;
		xmlConstant = XmlTagConstants.RESOURCES_PROCESS_FLOW;
		cameoDiagramConstant = CameoDiagramConstants.RESOURCES_PROCESS_FLOW;
	}
}
