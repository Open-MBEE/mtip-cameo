/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.CheckForNull;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;
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


  public ActivityNode(String name, String importId) {
    super(name, importId);

    this.attributeReferences.addAll(Arrays.asList(XML_TAG_ACTIVITY, XML_TAG_IN_STRUCTURED_NODE));
    this.attributeListReferences.addAll(Arrays.asList(XML_TAG_INCOMING, XML_TAG_IN_GROUP, XML_TAG_IN_PARTITION,
        XML_TAG_INTERRUPTIBLE_ACTIVITY_REGION, XML_TAG_OUTGOING, XML_TAG_REDEFINED_NODE));
  }
  
  @Override
  public void addReferences() {
    setActivity();
    setInStructuredNode();

    setIncoming();
    setInGroup();
    setInPartition();
    setInterruptibleActivityRegion();
    setOutgoing();
  }

  private void setActivity() {

  }

  private void setInStructuredNode() {

  }

  private void setIncoming() {

  }

  private void setInGroup() {

  }

  private void setInPartition() {
    if (getElementAsActivityNode() == null) {
      Logger.log(String.format("Could not set inPartition. Element %s could not be cast to ActivityNode.", element.getClass().getName()));
      return;
    }

    if (!xmlElement.hasListAttributes(XML_TAG_IN_PARTITION)) {
      return;
    }

    Collection<com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition> inPartitionElements =
        new ArrayList<com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition>();

    for (String inPartitionId : xmlElement.getListAttributes(XML_TAG_IN_PARTITION)) {
      Element inPartitionElement = Importer.getInstance().getImportedElement(inPartitionId);

      if (inPartitionElement == null) {
        continue;
      }

      if (!(inPartitionElement instanceof com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition)) {
        Logger.log(String.format("Cannot set inPartition. inPartition must be ActivityPartition not: %s; id: %s", element.getClass().getSimpleName(), inPartitionId));
        continue;
      }

      inPartitionElements.add((com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition) inPartitionElement);

    }

    getElementAsActivityNode().getInPartition().addAll(inPartitionElements);
  }

  private void setInterruptibleActivityRegion() {

  }

  private void setOutgoing() {

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

    com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode activityNode = getElementAsActivityNode();

    if (activityNode != null) {
      writeRelationship(relationships, activityNode.getActivity(), XML_TAG_ACTIVITY);
      writeRelationship(relationships, activityNode.getInStructuredNode(), XML_TAG_IN_STRUCTURED_NODE);

      writeRelationships(relationships, activityNode.getIncoming(), XML_TAG_INCOMING);
      writeRelationships(relationships, activityNode.getInGroup(), XML_TAG_IN_GROUP);
      writeRelationships(relationships, activityNode.getInPartition(), XML_TAG_IN_PARTITION);
      writeRelationships(relationships, activityNode.getInInterruptibleRegion(), XML_TAG_INTERRUPTIBLE_ACTIVITY_REGION);
      writeRelationships(relationships, activityNode.getOutgoing(), XML_TAG_OUTGOING);
      writeRelationships(relationships, activityNode.getRedefinedNode(), XML_TAG_REDEFINED_NODE);
    }

    return data;
  }

  @CheckForNull
  private com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode getElementAsActivityNode() {
    if (!(element instanceof com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode)) {
      return null;
    }

    return (com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode) element;
  }
}
