package org.aero.mtip.metamodel.dodaf.ov;

import org.aero.mtip.constants.DoDAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.AbstractDiagram;

public class OV1 extends AbstractDiagram {

	public OV1(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = DoDAFConstants.OV1;
		this.xmlConstant = XmlTagConstants.OV1;
		this.cameoDiagramConstant = "OV-1 High-Level Operational Concept Graphic";
	}
	
	@Override
	public String getCameoDiagramConstant() {
		return cameoDiagramConstant;
	}
	
	@Override
	public String getDiagramType() {
		return this.xmlConstant;
	}

}
