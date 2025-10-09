package org.aero.mtip.metamodel.dodaf.cv;

import org.aero.mtip.constants.DoDAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.profile.ClassDiagram;

public class CV2 extends ClassDiagram {

	public CV2(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = DoDAFConstants.CV2;
		this.xmlConstant = XmlTagConstants.CV2;
		this.cameoDiagramConstant = "CV-2 Capability Taxonomy";
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
