/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.sequence;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.ElementData;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.ConnectableElement;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.InteractionFragment;

public class Lifeline extends CommonElement {
  public static final String COVERED_BY = "coveredBy";
  public static final String REPRESENTS = "represents";
  
  public Lifeline(String name, String importId) {
    super(name, importId);
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = SysmlConstants.LIFELINE;
    this.xmlConstant = XmlTagConstants.LIFELINE;
    this.element = f.createLifelineInstance();

    this.attributeDependencies.add(REPRESENTS);
    this.attributeListDependencies.add(COVERED_BY);
  }

  @Override
  public Element createElement(Project project, Element owner, ElementData xmlElement) {
    super.createElement(project, owner, xmlElement);

    setRepresents();
    setCoveredBy();

    return element;
  }

  // TODO: Ensure typed by is set by setting represents
  private void setRepresents() {
    if (!elementData.hasAttribute(REPRESENTS)) {
      return;
    }
    
    Element representsElement = Importer.getInstance().getImportedElement(elementData.getAttribute(REPRESENTS));
   
    if (representsElement == null || !(representsElement instanceof ConnectableElement)) {
      return;
    }

    getLifeline().setRepresents((ConnectableElement) representsElement);
  }

  private void setCoveredBy() {
    if (!elementData.hasListAttributes(COVERED_BY)) {
      return;
    }
    
    for (String importedCoveredByImportID : elementData.getListAttributes(COVERED_BY)) {
      Element coveredByElement = Importer.getInstance().getImportedElement(importedCoveredByImportID);

      if (!(coveredByElement instanceof InteractionFragment)) {
        continue;
      }

      getLifeline().getCoveredBy().add((InteractionFragment) coveredByElement);
    }
  }

  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

    
    writeRelationship(relationships, getLifeline().getRepresents(), REPRESENTS);
    writeRelationships(relationships, getLifeline().getCoveredBy(), COVERED_BY);
    
    return data;
  }
  
  public com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.Lifeline getLifeline() {
    return (com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.Lifeline)element;
  }
}
