package org.aero.mtip.metamodel.uaf.operational;

import org.aero.mtip.constants.CameoDiagramConstants;
import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.AbstractDiagram;

public class OperationalInteractionScenarios extends AbstractDiagram {

  public OperationalInteractionScenarios(String name, String importId) {
    super(name, importId);

    metamodelConstant = UAFConstants.OPERATIONAL_INTERACTION_SCENARIOS;
    xmlConstant = XmlTagConstants.OPERATIONAL_INTERACTION_SCENARIOS;
    cameoDiagramConstant = CameoDiagramConstants.OPERATIONAL_INTERACTION_SCENARIOS;
  }
}
