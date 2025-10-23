/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */
package org.aero.mtip.metamodel.core.table;

import org.aero.mtip.constants.CameoConstants;
import org.aero.mtip.constants.CameoDiagramConstants;
import org.aero.mtip.constants.XmlTagConstants;

public class GlossaryTable extends AbstractTable {
	public GlossaryTable(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = CameoConstants.GLOSSARY_TABLE;
		this.xmlConstant = XmlTagConstants.GLOSSARY_TABLE;
		this.cameoConstant = CameoDiagramConstants.GLOSSARY_TABLE;
	}
}
