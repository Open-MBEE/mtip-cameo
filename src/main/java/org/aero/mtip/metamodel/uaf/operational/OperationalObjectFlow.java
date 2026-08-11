package org.aero.mtip.metamodel.uaf.operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.activity.ObjectFlow;

public class OperationalObjectFlow extends ObjectFlow {

  public OperationalObjectFlow(String name, String importId) {
    super(name, importId);

    metamodelConstant = UAFConstants.OPERATIONAL_OBJECT_FLOW;
    xmlConstant = XmlTagConstants.OPERATIONAL_OBJECT_FLOW;
  }
}
