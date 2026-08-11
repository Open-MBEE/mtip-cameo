package org.aero.mtip.metamodel.uaf.operational;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;

public class OperationalInformation extends CommonElement {

  public OperationalInformation(String name, String importId) {
    super(name, importId);

    element = f.createClassInstance();
    metamodelConstant = UAFConstants.OPERATIONAL_INFORMATION;
    xmlConstant = XmlTagConstants.OPERATIONAL_INFORMATION;
  }
}
