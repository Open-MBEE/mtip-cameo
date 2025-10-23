package org.aero.mtip.metamodel.uaf.personnel;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.general.Abstraction;

public class CompetenceForRole extends Abstraction {

	public CompetenceForRole(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.COMPETENCE_FOR_ROLE;
		this.xmlConstant = XmlTagConstants.COMPETENCE_FOR_ROLE;
	}

}
