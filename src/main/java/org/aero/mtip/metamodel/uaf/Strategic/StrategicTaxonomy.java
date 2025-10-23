package org.aero.mtip.metamodel.uaf.Strategic;

import org.aero.mtip.constants.CameoDiagramConstants;
import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.BlockDefinitionDiagram;

public class StrategicTaxonomy extends BlockDefinitionDiagram {

	public StrategicTaxonomy(String name, String importId) {
		super(name, importId);
		 this.metamodelConstant = UAFConstants.STRATEGIC_TAXONOMY_DIAGRAM;
		 this.xmlConstant = XmlTagConstants.STRATEGIC_TAXONOMY_DIAGRAM;
		 this.cameoDiagramConstant = CameoDiagramConstants.STRATEGIC_TAXONOMY;
	}
}