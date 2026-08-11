package org.aero.mtip.metamodel.uaf.parameters;

import org.aero.mtip.constants.CameoDiagramConstants;
import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.BlockDefinitionDiagram;

public class EnvironmentDiagram extends BlockDefinitionDiagram {
  public EnvironmentDiagram(String name, String importId) {
    super(name, importId);
    this.metamodelConstant = UAFConstants.ENVIRONMENT_DIAGRAM;
    this.xmlConstant = XmlTagConstants.ENVIRONMENT_DIAGRAM;
    this.cameoDiagramConstant = CameoDiagramConstants.ENVIRONMENT;
  }
}
