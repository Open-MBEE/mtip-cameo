package org.aero.mtip.metamodel.uaf.operational;

import org.aero.mtip.constants.CameoDiagramConstants;
import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.BlockDefinitionDiagram;

public class OperationalInformationDiagram extends BlockDefinitionDiagram {

  public OperationalInformationDiagram(String name, String importId) {
    super(name, importId);
    
    metamodelConstant = UAFConstants.OPERATIONAL_INFORMATION_DIAGRAM;
    xmlConstant = XmlTagConstants.OPERATIONAL_INFORMATION_DIAGRAM;
    cameoDiagramConstant = CameoDiagramConstants.OPERATIONAL_INFORMATION_DIAGRAM;
  }
}
