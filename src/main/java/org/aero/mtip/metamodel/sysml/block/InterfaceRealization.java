/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.block;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonDirectedRelationship;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.MtipUtils;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class InterfaceRealization extends CommonDirectedRelationship {

  public InterfaceRealization(String name, String importId) {
    super(name, importId);
    creationType = XmlTagConstants.ELEMENTS_FACTORY;
    metamodelConstant = SysmlConstants.INTERFACE_REALIZATION;
    xmlConstant = XmlTagConstants.INTERFACE_REALIZATION;
    element = f.createInterfaceRealizationInstance();
  }

  @Override
  public void setOwner(Element owner) {
    if (owner == null && ModelHelper.canMoveChildInto(owner, element)) {
      element.setOwner(owner);
    }

    try {
      ModelElementsManager.getInstance().addElement(element, owner);
    } catch (ReadOnlyElementException e) {
      Logger.logException(e);
    }
  }
  
  @Override
  public void setClient(Element supplier) {
    super.setClient(supplier);
    
    if (!(supplier instanceof com.nomagic.uml2.ext.magicdraw.classes.mdinterfaces.Interface)) {
      Logger.log(String.format("Expected interface as supplier not %s", MtipUtils.getEntityType(supplier)));
      return;
    }
    
    getInterfaceRealization().setContract((com.nomagic.uml2.ext.magicdraw.classes.mdinterfaces.Interface)supplier);
  }
  
  public com.nomagic.uml2.ext.magicdraw.classes.mdinterfaces.InterfaceRealization getInterfaceRealization() {
    return (com.nomagic.uml2.ext.magicdraw.classes.mdinterfaces.InterfaceRealization)element;
  }
}
