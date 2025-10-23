package org.aero.mtip.metamodel.dodaf.sv;

import org.aero.mtip.constants.DoDAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.profile.ClassDiagram;

public class SV2 extends ClassDiagram {

	public SV2(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = DoDAFConstants.SV2;
		this.xmlConstant = XmlTagConstants.SV2;
		this.cameoDiagramConstant = "SV-2 Systems Communications Description";
	}
	
	@Override
	public String getCameoDiagramConstant() {
		//Going to need to find what Cameo calls DoDAF Diagrams
		return cameoDiagramConstant;
	}
	
	@Override
	public String getDiagramType() {
		return this.xmlConstant;
	}

}
