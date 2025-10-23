package org.aero.mtip.metamodel.uaf.Resources;

import org.aero.mtip.constants.CameoDiagramConstants;
import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.block.BlockDefinitionDiagram;

public class ResourcesConnectivity extends BlockDefinitionDiagram{
    public ResourcesConnectivity(String name, String importId) {
         super(name, importId);
         this.metamodelConstant = UAFConstants.RESOURCES_CONNECTIVITY_DIAGRAM;
         this.xmlConstant = XmlTagConstants.RESOURCES_CONNECTIVITY_DIAGRAM;
         this.cameoDiagramConstant = CameoDiagramConstants.RESOURCES_CONNECTIVITY;
    }
}