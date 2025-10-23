package org.aero.mtip.metamodel.uml;

import org.aero.mtip.constants.UmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.profiles.MagicDraw;

public class Legend extends CommonElement {
  public Legend(String name, String importId) {
    super(name, importId);
    
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = UmlConstants.LEGEND;
    this.xmlConstant = XmlTagConstants.LEGEND;
    this.element = f.createClassInstance();
    this.creationStereotype = MagicDraw.getLegendStereotype();
  }
}
