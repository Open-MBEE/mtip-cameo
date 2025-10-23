package org.aero.mtip.metamodel.dodaf.cv;

import org.aero.mtip.constants.DoDAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.profile.ClassDiagram;

public class CV5 extends ClassDiagram {

	public CV5(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = DoDAFConstants.CV5;
		this.xmlConstant = XmlTagConstants.CV5;
		this.cameoDiagramConstant = "DODAF2_CV-5";
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