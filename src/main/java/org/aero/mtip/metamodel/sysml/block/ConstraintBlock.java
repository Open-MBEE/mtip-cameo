/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.block;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.profiles.SysML;

public class ConstraintBlock extends CommonElement {
  public ConstraintBlock(String name, String EAID) {
    super(name, EAID);
    this.creationType = XmlTagConstants.CLASS_WITH_STEREOTYPE;
    this.metamodelConstant = SysmlConstants.CONSTRAINT_BLOCK;
    this.xmlConstant = XmlTagConstants.CONSTRAINT_BLOCK;
    this.creationStereotype = SysML.getConstraintBlockStereotype();
  }
}
