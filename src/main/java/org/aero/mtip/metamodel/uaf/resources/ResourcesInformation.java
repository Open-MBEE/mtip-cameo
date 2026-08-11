package org.aero.mtip.metamodel.uaf.resources;

import org.aero.mtip.constants.CameoDiagramConstants;
import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.BlockDefinitionDiagram;

public class ResourcesInformation extends BlockDefinitionDiagram {

  public ResourcesInformation(String name, String importId) {
    super(name, importId);
    
    metamodelConstant = UAFConstants.RESOURCES_INFORMATION_DIAGRAM;
    xmlConstant = XmlTagConstants.RESOURCES_INFORMATION;
    cameoDiagramConstant = CameoDiagramConstants.RESOURCES_INFORMATION;
  }
}
