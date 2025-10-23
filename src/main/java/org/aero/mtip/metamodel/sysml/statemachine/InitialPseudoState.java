/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.statemachine;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;

import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.PseudostateKindEnum;

public class InitialPseudoState extends PseudoState {
	
	public InitialPseudoState(String name, String importId) {
		super(name, importId);
		this.psKind =  PseudostateKindEnum.INITIAL;
		this.metamodelConstant = SysmlConstants.INITIAL_PSEUDO_STATE;
		this.xmlConstant = XmlTagConstants.INITIAL_PSEUDO_STATE;
	}
}
