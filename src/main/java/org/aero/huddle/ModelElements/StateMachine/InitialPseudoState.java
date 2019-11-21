package org.aero.huddle.ModelElements.StateMachine;

import org.aero.huddle.util.XmlTagConstants;

import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.PseudostateKindEnum;

public class InitialPseudoState extends PseudoState {
	
	public InitialPseudoState(String name, String EAID) {
		super(name, EAID);
		this.psKind =  PseudostateKindEnum.INITIAL;
		this.xmlTag = XmlTagConstants.INITIALPSEUDOSTATE;
	}
}
