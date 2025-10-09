package org.aero.mtip.metamodel.uaf.Strategic;

import org.aero.mtip.constants.CameoDiagramConstants;
import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.BlockDefinitionDiagram;

public class StrategicConstraints extends BlockDefinitionDiagram {

	public StrategicConstraints(String name, String importId) {
		 super(name, importId);
		 this.metamodelConstant = UAFConstants.STRATEGIC_CONSTRAINTS_DIAGRAM;
		 this.xmlConstant = XmlTagConstants.STRATEGIC_CONSTRAINTS_DIAGRAM;
		 this.cameoDiagramConstant = CameoDiagramConstants.STRATEGIC_CONSTRAINTS;
	}
}
