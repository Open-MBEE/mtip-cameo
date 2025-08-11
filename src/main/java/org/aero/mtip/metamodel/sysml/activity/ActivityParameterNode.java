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
import org.aero.mtip.metamodel.core.CommonElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class ActivityParameterNode extends CommonElement {
  public ActivityParameterNode(String name, String EAID) {
    super(name, EAID);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.ACTIVITY_PARAMETER_NODE;
    this.xmlConstant = XmlTagConstants.ACTIVITY_PARAMETER_NODE;
    this.element = f.createActivityParameterNodeInstance();
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityParameterNode activityParameterNode =
        getElementAsActivityParameterNode();

    if (activityParameterNode != null) {
      writeRelationship(relationships, activityParameterNode.getActivity(), ActivityNode.XML_TAG_ACTIVITY);
      writeRelationship(relationships, activityParameterNode.getInStructuredNode(),
          ActivityNode.XML_TAG_IN_STRUCTURED_NODE);

      writeRelationships(relationships, activityParameterNode.getIncoming(), ActivityNode.XML_TAG_INCOMING);
      writeRelationships(relationships, activityParameterNode.getInGroup(), ActivityNode.XML_TAG_IN_GROUP);
      writeRelationships(relationships, activityParameterNode.getInPartition(),
          ActivityNode.XML_TAG_IN_PARTITION);
      writeRelationships(relationships, activityParameterNode.getInInterruptibleRegion(),
          ActivityNode.XML_TAG_INTERRUPTIBLE_ACTIVITY_REGION);
      writeRelationships(relationships, activityParameterNode.getOutgoing(), ActivityNode.XML_TAG_OUTGOING);
      writeRelationships(relationships, activityParameterNode.getRedefinedNode(),
          ActivityNode.XML_TAG_REDEFINED_NODE);
    }

    return data;
  }

  @CheckForNull
  public com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityParameterNode getElementAsActivityParameterNode() {
    if (!(element instanceof com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityParameterNode)) {
      return null;
    }
    
    return (com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityParameterNode) element;

  }
}
