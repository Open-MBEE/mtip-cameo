/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.activity;

import java.util.HashMap;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.XMLItem;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public abstract class ActivityNode extends CommonElement {
  public static final String XML_TAG_ACTIVITY = "activity";
  public static final String XML_TAG_INCOMING = "incoming";
  public static final String XML_TAG_IN_GROUP = "inGroup";
  public static final String XML_TAG_IN_PARTITION = "inPartition";
  public static final String XML_TAG_IN_STRUCTURED_NODE = "inStructuredNode";
  public static final String XML_TAG_INTERRUPTIBLE_ACTIVITY_REGION = "interruptibleActivityRegion";
  public static final String XML_TAG_OUTGOING = "outgoing";
  public static final String XML_TAG_REDEFINED_NODE = "redefinedNode";


  public ActivityNode(String name, String EAID) {
    super(name, EAID);
  }

  public Element createElement(Project project, Element owner, XMLItem xmlElement) {
    super.createElement(project, owner, xmlElement);
    if (xmlElement != null) {
      if (xmlElement.hasAttribute(XmlTagConstants.ATTRIBUTE_NAME_INTERRUPTIBLE_ACTIVITY_REGION)) {
        // com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode
        // activityNode =
        // (com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode) element;
        // Add In Interruptible Region here if possible - may need to nest in diagram to achieve
        // this.
      }
    }
    return element;
  }

  @Override
  public void createDependentElements(HashMap<String, XMLItem> parsedXML, XMLItem modelElement) {
    super.createDependentElements(parsedXML, modelElement);

    if (modelElement.hasAttribute(XML_TAG_ACTIVITY)) {
      String activityId = modelElement.getAttribute(XML_TAG_ACTIVITY);
      Importer.getInstance().buildElement(parsedXML, parsedXML.get(activityId));
    }

    if (modelElement.hasAttribute(XmlTagConstants.ATTRIBUTE_NAME_INTERRUPTIBLE_ACTIVITY_REGION)) {
      String iarID =
          modelElement.getAttribute(XmlTagConstants.ATTRIBUTE_NAME_INTERRUPTIBLE_ACTIVITY_REGION);
      Importer.getInstance().buildElement(parsedXML, parsedXML.get(iarID));
    }
  }

  @Override
  public void setOwner(Element owner) {
    if (!(owner instanceof Activity)) {
      owner = CameoUtils.findNearestActivity(owner);
    }

    element.setOwner(owner);
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode activityNode =
        getElementAsActivityNode();

    if (activityNode != null) {
      writeRelationship(relationships, activityNode.getActivity(), XML_TAG_ACTIVITY);
      writeRelationship(relationships, activityNode.getInStructuredNode(),
          XML_TAG_IN_STRUCTURED_NODE);

      writeRelationships(relationships, activityNode.getIncoming(), XML_TAG_INCOMING);
      writeRelationships(relationships, activityNode.getInGroup(), XML_TAG_IN_GROUP);
      writeRelationships(relationships, activityNode.getInPartition(), XML_TAG_IN_PARTITION);
      writeRelationships(relationships, activityNode.getInInterruptibleRegion(),
          XML_TAG_INTERRUPTIBLE_ACTIVITY_REGION);
      writeRelationships(relationships, activityNode.getOutgoing(), XML_TAG_OUTGOING);
      writeRelationships(relationships, activityNode.getRedefinedNode(), XML_TAG_REDEFINED_NODE);
    }

    return data;
  }

  private com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode getElementAsActivityNode() {
    if (!(element instanceof com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode)) {
      return null;
    }

    return (com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode) element;
  }
}
