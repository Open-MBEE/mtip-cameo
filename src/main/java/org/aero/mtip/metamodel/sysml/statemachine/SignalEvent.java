/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.statemachine;

import javax.annotation.CheckForNull;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.ElementData;
import org.aero.mtip.util.Logger;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Signal;

public class SignalEvent extends CommonElement {
  public static final String SIGNAL = "signal";
  
  public SignalEvent(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.SIGNAL_EVENT;
    this.xmlConstant = XmlTagConstants.SIGNAL_EVENT;
    this.element = f.createSignalEventInstance();

    this.attributeDependencies.add(SIGNAL);
  }

  @Override
  public Element createElement(Project project, Element owner, ElementData xmlElement) {
    super.createElement(project, owner, xmlElement);
    
    setSignal();
    
    return element;
  }

  private void setSignal() {
    if (!elementData.hasAttribute(SIGNAL)) {
      return;
    }

    com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.SignalEvent signalEvent = getElementAsSignalEvent();

    if (signalEvent == null) {
      Logger.log(String.format("Failed to cast expected signal event to signal event with id: %s", importId));
      return;
    }

    Element signal = Importer.getInstance().getImportedElement(elementData.getAttribute(XmlTagConstants.SIGNAL_TAG));

    if (signal == null || !(signal instanceof Signal)) {
      Logger.log(String.format("Failed to created signal for %s", element.getClassType().getName()));
      return;
    }

    signalEvent.setSignal((Signal) signal);

  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    writeSignal(relationships, element);

    return data;
  }

  protected void writeSignal(org.w3c.dom.Element relationships, Element element) {
    com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.SignalEvent signalEvent =
        (com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.SignalEvent) element;
    Signal signal = signalEvent.getSignal();

    if (signal == null) {
      return;
    }

    org.w3c.dom.Element signalTag = XmlWriter.createMtipRelationship(signal, SIGNAL);
    XmlWriter.add(relationships, signalTag);
  }

  @CheckForNull
  private com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.SignalEvent getElementAsSignalEvent() {
    if (!(element instanceof com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.SignalEvent)) {
      return null;
    }

    return (com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.SignalEvent) element;
  }
}
