/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */
package org.aero.mtip.metamodel.sysml.statemachine;
import java.util.Collection;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonDirectedRelationship;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdbasicbehaviors.FunctionBehavior;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Vertex;

public class Transition extends CommonDirectedRelationship {
  public static final String CONTAINER = "container";
  public static final String EFFECT = "effect";
  public static final String GUARD = "guard";
  public static final String TRIGGER = "trigger";

  public Transition(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.xmlConstant = XmlTagConstants.TRANSITION;
    this.metamodelConstant = SysmlConstants.TRANSITION;
    this.element = f.createTransitionInstance();

    this.attributeDependencies.add(CONTAINER);
    this.attributeReferences.add(TRIGGER);
  }

  @Override
  public void addReferences() {
    super.addReferences();

    setGuard();
    setEffect();
    setTrigger();
  }

  public void setGuard() {
    if (!elementData.hasAttribute(GUARD)) {
      return;
    }

    Constraint constraint = f.createConstraintInstance();
    LiteralString specification = f.createLiteralStringInstance();

    specification.setValue(elementData.getAttribute(GUARD));
    constraint.setSpecification(specification);

    getTransition().setGuard(constraint);
  }

  public void setEffect() {
    if (!elementData.hasAttribute(EFFECT)) {
      return;
    }

    FunctionBehavior functionBehavior = f.createFunctionBehaviorInstance();
    functionBehavior.getBody().add(elementData.getAttribute(EFFECT));
    getTransition().setEffect(functionBehavior);
  }

  public void setTrigger() {
    if (!elementData.hasAttribute(TRIGGER)) {
      return;
    }

    com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger trigger =
        (com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger) Importer.getInstance()
            .buildEntity(elementData.getAttribute(TRIGGER));

    if (trigger == null) {
      Logger.log(String.format("Trigger data found, but failed to create trigger for transition %s", importId));
      return;
    }

    getTransition().getTrigger().add(trigger);
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    writeContainer(relationships, element);
    writeTrigger(relationships, element);
    
    return data;
  }

  protected void writeContainer(org.w3c.dom.Element relationships, Element element) {
    XmlWriter.add(relationships, XmlWriter.createMtipRelationship(getTransition().getContainer(), CONTAINER));
  }

  protected void writeTrigger(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Transition tr =
        (com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Transition) element;
    Collection<com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger> triggers = tr.getTrigger();

    if (triggers.isEmpty()) {
      return;
    }

    com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger trigger = triggers.iterator().next();
    org.w3c.dom.Element triggerTag = XmlWriter.createMtipRelationship(trigger, XmlTagConstants.TRIGGER_TAG);
    XmlWriter.add(relationships, triggerTag);
  }

  @Override
  public void setOwner(Element owner) {
    if (!(owner instanceof com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Region)) {
      owner = CameoUtils.findNearestRegion(project, getSupplier());
    }

    element.setOwner(owner);
  }

  @Override
  public void setSupplier(Element supplier) {
    getTransition().setSource((Vertex) supplier);
  }

  @Override
  public void setClient(Element client) {
    getTransition().setTarget((Vertex) client);
  }

  @Override
  public Element getSupplier() {
    return getTransition().getSource();
  }

  @Override
  public Element getClient() {
    return getTransition().getTarget();
  }

  public com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Transition getTransition() {
    return (com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Transition) element;
  }
}
