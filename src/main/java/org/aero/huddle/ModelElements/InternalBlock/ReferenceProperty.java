package org.aero.huddle.ModelElements.InternalBlock;

import org.aero.huddle.ModelElements.CommonElement;
import org.aero.huddle.util.XMLItem;
import org.aero.huddle.util.XmlTagConstants;
import org.w3c.dom.Document;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class ReferenceProperty extends CommonElement {

	public ReferenceProperty(String name, String EAID) {
		super(name, EAID);
	}

	@Override
	public Element createElement(Project project, Element owner, XMLItem xmlElement) {
		Profile mdCustomizationProfile = StereotypesHelper.getProfile(project, "MD Customization for SysML"); 
		Stereotype referencePropertyStereotype = StereotypesHelper.getStereotype(project, "ReferenceProperty", mdCustomizationProfile);
		
		if (!SessionManager.getInstance().isSessionCreated(project)) {
			SessionManager.getInstance().createSession(project, "Create Reference Property Element");
		}
		
		com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property prop = project.getElementsFactory().createPropertyInstance();
		prop.setName(name);
		prop.setOwner(owner);
		StereotypesHelper.addStereotype(prop, referencePropertyStereotype);
		
		SessionManager.getInstance().closeSession(project);
		return prop;
	}

	@Override
	public void writeToXML(Element element, Project project, Document xmlDoc) {
		org.w3c.dom.Element data = createBaseXML(element, xmlDoc);
		
		//org.w3c.dom.Element attributes = getAttributes(data.getChildNodes());
		
		// Create type field for Sysml model element types
		org.w3c.dom.Element type = xmlDoc.createElement("type");
		type.appendChild(xmlDoc.createTextNode(XmlTagConstants.REFERENCEPROPERTY));
		data.appendChild(type);
		
		org.w3c.dom.Element root = (org.w3c.dom.Element) xmlDoc.getFirstChild();
		root.appendChild(data);
	}
}