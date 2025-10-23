/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.block;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.profiles.MDCustomizationForSysML;
import org.aero.mtip.profiles.SysML;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class PartProperty extends org.aero.mtip.metamodel.sysml.sequence.Property {
  public PartProperty(String name, String importId) {
    super(name, importId);
    this.metamodelConstant = SysmlConstants.PART_PROPERTY;
    this.xmlConstant = XmlTagConstants.PART_PROPERTY;
    this.element = f.createPropertyInstance();
    this.creationStereotype = MDCustomizationForSysML.getPartPropertyStereotype();
  }

  @Override
  public void setOwner(Element owner) {
    if (owner == null) {
      Logger.log(String.format("Cannot set owner. Owner is null for %s; id: %s", getClass().getSimpleName(), importId));
    }
    
    try {
      if (!(SysML.isBlock(owner))) {
        owner = CameoUtils.findNearestBlock(project, owner);
        if (owner == null) {
          String logMessage = "Invalid parent. Parent must be block " + name + " with id " + importId
              + ". No parents found in ancestors. Element could not be placed in model.";
          Logger.log(logMessage);

        }
        element.setOwner(owner);
      } else {
        element.setOwner(owner);
      }
    } catch (IllegalArgumentException iae) {
      String logMessage =
          "Invalid parent. Parent must be block " + name + " with id " + importId + ". Element could not be placed in model.";
      Logger.log(logMessage);
    }
  }
}
