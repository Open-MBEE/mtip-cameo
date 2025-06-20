/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.block;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.profiles.SysML;
import org.aero.mtip.util.XMLItem;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.VisibilityKindEnum;

public class FlowPort extends Port {

  public FlowPort(String name, String EAID) {
    super(name, EAID);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.FLOW_PORT;
    this.xmlConstant = XmlTagConstants.FLOW_PORT;
    this.element = f.createPortInstance();
    this.creationStereotype = SysML.getFlowPortStereotype();
  }

  @Override
  public Element createElement(Project project, Element owner, XMLItem xmlElement) {
    com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port port =
        (com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port) super.createElement(
            project, owner, xmlElement);
    port.setVisibility(VisibilityKindEnum.PRIVATE);

    return port;
  }
}
