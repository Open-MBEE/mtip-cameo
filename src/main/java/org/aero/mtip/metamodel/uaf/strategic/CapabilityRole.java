package org.aero.mtip.metamodel.uaf.strategic;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.sequence.Property;

public class CapabilityRole extends Property {

  public CapabilityRole(String name, String importId) {
    super(name, importId);

    metamodelConstant = UAFConstants.CAPABILITY_ROLE;
    xmlConstant = XmlTagConstants.CAPABILITY_ROLE;
  }
}
