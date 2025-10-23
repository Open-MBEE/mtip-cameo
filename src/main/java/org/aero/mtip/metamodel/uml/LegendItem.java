package org.aero.mtip.metamodel.uml;

import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.UmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.sysml.profile.Constraint;
import org.aero.mtip.profiles.MagicDraw;
import com.nomagic.uml2.MagicDrawProfile;
import com.nomagic.uml2.MagicDrawProfile.LegendItemStereotype;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class LegendItem extends Constraint {
  
  public LegendItem(String name, String importId) {
    super(name, importId);
    
    this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
    this.metamodelConstant = UmlConstants.LEGEND_ITEM;
    this.xmlConstant = XmlTagConstants.LEGEND_ITEM;
    this.element = f.createClassInstance();
    this.creationStereotype = MagicDraw.getLegendItemStereotype();
  }
  
  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element attributes = getAttributes(data.getChildNodes());

    XmlWriter.writeAttributes(attributes, getLegendItemStereotypeWrapper().getAdornedProperties(element), name);
    
    return data;
  }
  
  private LegendItemStereotype getLegendItemStereotypeWrapper() {
    return MagicDrawProfile.getInstance(element).legendItem();
  }
}
