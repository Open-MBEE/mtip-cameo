/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.activity;

import javax.annotation.CheckForNull;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.MtipUtils;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class ObjectNode extends ActivityNode {

  public ObjectNode(String name, String importId) {
    super(name, importId);

    creationType = XmlTagConstants.ELEMENTS_FACTORY;
    metamodelConstant = SysmlConstants.CENTRAL_BUFFER_NODE;
    xmlConstant = XmlTagConstants.CENTRAL_BUFFER_NODE;
    element = f.createCallBehaviorActionInstance();
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ObjectNode objectNode =
        getElementAsObjectNode();
    
    if (objectNode == null) {
      Logger.log(String.format("Cannot convert expected object node to ObjectNode for element with id %s.", MtipUtils.getId(element)));
      return data;
    }
    
    return data;
  }
  
  @CheckForNull
  public com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ObjectNode getElementAsObjectNode() {
    if (!(element instanceof com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ObjectNode)) {
      return null;
    }

    return (com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ObjectNode) element;

  }
}
