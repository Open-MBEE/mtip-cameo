/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.activity;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.util.CameoUtils;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class OutputPin extends ActivityNode {

  public OutputPin(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.OUTPUT_PIN;
    this.xmlConstant = XmlTagConstants.OUTPUT_PIN;
    this.element = f.createOutputPinInstance();
  }

  @Override
  public void setOwner(Element owner) {
    if (!(owner instanceof com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.Action)) {
      owner = CameoUtils.findNearestActivity(owner);
    }

    element.setOwner(owner);
  }
}
