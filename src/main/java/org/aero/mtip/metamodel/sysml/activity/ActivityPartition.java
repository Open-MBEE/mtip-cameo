/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.activity;

import java.util.Arrays;
import java.util.Collections;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.XMLItem;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class ActivityPartition extends CommonElement {
  public static final String XML_TAG_EDGE = "edge";
  public static final String XML_TAG_NODE = "node";
  public static final String XML_TAG_REPRESENTS = "represents";
  public static final String XML_TAG_SUBPARTITION = "subpartition";
  public static final String XML_TAG_SUPER_PARTITION = "superpartition";

  public ActivityPartition(String name, String importId) {
    super(name, importId);
    
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.ACTIVITY_PARTITION;
    this.xmlConstant = XmlTagConstants.ACTIVITY_PARTITION;
    this.element = f.createActivityPartitionInstance();
    
    this.attributeDependencies = Arrays.asList(XML_TAG_REPRESENTS,
        XML_TAG_SUPER_PARTITION, XML_TAG_EDGE, XML_TAG_NODE, XML_TAG_SUBPARTITION);
  }

  @Override
  public Element createElement(Project project, Element owner, XMLItem xmlElement) {
    super.createElement(project, owner, xmlElement);

    setRepresents();
    setSuperPartition();

    return element;
  }

  private void setRepresents() {
    if (!xmlElement.hasAttribute(XML_TAG_REPRESENTS)) {
      return;
    }

    Element represents =
        Importer.getInstance().getImportedElement(xmlElement.getAttribute(XML_TAG_REPRESENTS));

    if (represents == null) {
      return;
    }

    getElementAsActivityPartition().setRepresents(represents);
  }

  private void setSuperPartition() {
    if (!xmlElement.hasAttribute(XML_TAG_SUPER_PARTITION)) {
      return;
    }

    Element superPartition =
        Importer.getInstance().getImportedElement(xmlElement.getAttribute(XML_TAG_SUPER_PARTITION));

    if (superPartition == null
        || !(superPartition instanceof com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition)) {
      return;
    }

    getElementAsActivityPartition().setSuperPartition(
        (com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition) superPartition);
  }

  @Override
  public void setOwner(Element owner) {
    if (!(owner instanceof Activity)) {
      owner = CameoUtils.findNearestActivity(owner);
    }

    if (owner == null) {
      ModelHelper.dispose(Collections.singletonList(element));
      return;
    }

    element.setOwner(owner);
    com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition ap =
        (com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition) element;
    ap.setInActivity(
        (com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity) owner);
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition activityPartition =
        getElementAsActivityPartition();

    if (activityPartition == null) {
      Logger.log(String.format(
          "Unable to cast %s to ActivityPartition. Cannot export relationship elements of ActivityPartition.",
          this.getClass().toString()));
      return data;
    }

    writeRelationship(relationships, activityPartition.getRepresents(), XML_TAG_REPRESENTS);
    writeRelationship(relationships, activityPartition.getSuperPartition(),
        XML_TAG_SUPER_PARTITION);

    writeRelationships(relationships, activityPartition.getEdge(), XML_TAG_EDGE);
    writeRelationships(relationships, activityPartition.getNode(), XML_TAG_NODE);
    writeRelationships(relationships, activityPartition.getSubpartition(), XML_TAG_SUBPARTITION);

    return data;
  }

  private com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition getElementAsActivityPartition() {
    if (!(element instanceof com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition)) {
      return null; // ActivityParameterNodes are ActivityNodes but cannot be cast.
    }

    return (com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition) element;
  }
}
