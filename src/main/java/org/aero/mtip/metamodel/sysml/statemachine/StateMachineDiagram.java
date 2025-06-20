/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.statemachine;

import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.AbstractDiagram;
import com.nomagic.magicdraw.sysml.util.SysMLConstants;

public class StateMachineDiagram extends AbstractDiagram {
  public StateMachineDiagram(String name, String EAID) {
    super(name, EAID);
    this.xmlConstant = XmlTagConstants.STATEMACHINEDIAGRAM;
    this.cameoDiagramConstant = SysMLConstants.SYSML_STATE_MACHINE_DIAGRAM;
  }
}
