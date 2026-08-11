package org.aero.mtip.metamodel.uaf.resources;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;

public class ResourceInformation extends CommonElement {

  public ResourceInformation(String name, String importId) {
    super(name, importId);

    element = f.createClassInstance();
    metamodelConstant = UAFConstants.RESOURCE_INFORMATION;
    xmlConstant = XmlTagConstants.RESOURCE_INFORMATION;
  }
}
