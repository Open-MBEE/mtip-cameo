/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.metamodel.sysml.profile;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.XMLItem;
import org.w3c.dom.NodeList;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.uml.Finder;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class MetaClass extends CommonElement {

  public MetaClass(String name, String importId) {
    super(name, importId);
    this.metamodelConstant = SysmlConstants.METACLASS;
    this.xmlConstant = XmlTagConstants.METACLASS;
  }

  @Override
  public Element createElement(Project project, Element owner, XMLItem xmlElement) {
    com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class metaclass = StereotypesHelper.getMetaClassByName(project, this.name);

    // TODO: Confirm that this method gets Metaclasses from all profiles.
    if (metaclass != null) {
      return metaclass; 
    }
    
    Logger.log(String.format(
        "Could not find metaclass by name or id. Creating metaclass with name: %s; id: %s",
        this.name, xmlElement.getImportId()));

    element = f.createClassInstance();
    Profile standardProfile = StereotypesHelper.getProfile(project, "StandardProfile");
    
    Stereotype metaclassStereotype =
        StereotypesHelper.getStereotype(project, "Metaclass", standardProfile);
    StereotypesHelper.addStereotype(element, metaclassStereotype);
    ((NamedElement) element).setName(name);

    return element;
  }
  
  @Override
  public org.w3c.dom.Element writeToXML(Element element) {
    org.w3c.dom.Element data = super.writeToXML(element);
    org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());
    
    NodeList hasParentTags = relationships.getElementsByTagName(XmlTagConstants.HAS_PARENT);
    
    for (int i = 0; i < hasParentTags.getLength(); i++) {
      relationships.removeChild(hasParentTags.item(i));
    }

    return data;
  }

}
