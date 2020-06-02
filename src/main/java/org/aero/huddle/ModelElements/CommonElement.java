package org.aero.huddle.ModelElements;

import java.util.HashMap;
import java.util.List;

import org.aero.huddle.util.CameoUtils;
import org.aero.huddle.util.ImportLog;
import org.aero.huddle.util.SysmlConstants;
import org.aero.huddle.util.XMLItem;
import org.aero.huddle.util.XmlTagConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.TypedElement;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Pseudostate;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Region;
import com.nomagic.uml2.impl.ElementsFactory;

public abstract class CommonElement {
	public static final String invalidParentRoot = "Invalid parent - not SysML compliant."; 
	
	protected String name;
	protected String EAID;
	protected String sysmlConstant;
	protected String xmlConstant;
	protected String creationType;
	protected ElementsFactory f;
	protected Element sysmlElement;
	
	protected String requiredParentType;
	protected String invalidParentMessage;
	
	public CommonElement(String name, String EAID) {
		this.EAID = EAID;
		this.name = name;
		this.f = Application.getInstance().getProject().getElementsFactory();
	}
	
	public Element createElement(Project project, Element owner, XMLItem xmlElement) {
		if(this.creationType.contentEquals(XmlTagConstants.ELEMENTSFACTORY)) {
			return createElementByElementFactory(project, owner, xmlElement);
		}
		return null;
	}
	
	protected Element createElementByElementFactory(Project project, Element owner, XMLItem xmlElement) {
		ElementsFactory f = project.getElementsFactory();
		if (!SessionManager.getInstance().isSessionCreated(project)) {
			SessionManager.getInstance().createSession(project, "Create " +  this.sysmlConstant + " Element");
		}
		
		if(sysmlElement != null) {
			((NamedElement)sysmlElement).setName(name);
			if(owner != null) {
				try {
					sysmlElement.setOwner(owner);
				} catch(IllegalArgumentException iae){
					setInvalidParentMessage(owner);
					ImportLog.log(invalidParentMessage);
					sysmlElement.dispose();
				}	
			} else {
				try {
					sysmlElement.setOwner(owner);
				} catch(IllegalArgumentException iae){
					String logMessage = "Invalid parent. No parent provided and primary model invalid parent for " + name + " with id " + EAID + ". Element could not be placed in model.";
					CameoUtils.logGUI(invalidParentMessage);
					ImportLog.log(invalidParentMessage);
					sysmlElement.dispose();
				}
			}
			
			SessionManager.getInstance().closeSession(project);
			return sysmlElement;
		} else {
			ImportLog.log("SysmlConstants type not set for class " + xmlElement.getType() + ". Elements Factory could not create element");
		}
		return null;
	}
	
	public void writeToXML(Element element, Project project, Document xmlDoc) {
		org.w3c.dom.Element data = createBaseXML(element, xmlDoc);
				
		// Create type field for Sysml model element types
		org.w3c.dom.Element type = xmlDoc.createElement("type");
		type.appendChild(xmlDoc.createTextNode(this.xmlConstant));
		data.appendChild(type);
		
		org.w3c.dom.Element root = (org.w3c.dom.Element) xmlDoc.getFirstChild();
		root.appendChild(data);
	}
	
	public static Element createClassWithStereotype(Project project, String name,  Stereotype stereotype, Element owner) {;
		boolean externalSession = false;
		if (!SessionManager.getInstance().isSessionCreated(project)) {
			SessionManager.getInstance().createSession(project, "Create Class Element");
		} else {
			externalSession = true;
		}
	
		Class sysmlElement = project.getElementsFactory().createClassInstance();
		sysmlElement.setName(name);
		if (stereotype != null) {
			StereotypesHelper.addStereotype(sysmlElement, stereotype);
		}
	
		if (owner != null) {
			sysmlElement.setOwner(owner);
		} else {
			sysmlElement.setOwner(project.getPrimaryModel());
		}
		
		if(!externalSession) {
			SessionManager.getInstance().closeSession(project);
		}
		return sysmlElement;
	}
	
	public Element createElementFromElementsFactory(Project project, Element owner) {
		ElementsFactory f = project.getElementsFactory();
		if (!SessionManager.getInstance().isSessionCreated(project)) {
			SessionManager.getInstance().createSession(project, "Create Class Element");
		}
		
		Element sysmlElement = f.createClassInstance();
		((NamedElement)sysmlElement).setName(name);
		
		if(owner != null) {
			sysmlElement.setOwner(owner);
		} else {
			sysmlElement.setOwner(project.getPrimaryModel());
		}
		
		SessionManager.getInstance().closeSession(project);
		return sysmlElement;
	}
	
	public HashMap<String, Element> createElementInstanceMap() {
		return null;
	}
	
	public static Region createRegion(Project project, Element owner) {
		if (!SessionManager.getInstance().isSessionCreated(project)) {
			SessionManager.getInstance().createSession(project, "Create Region Element");
		}
		ElementsFactory elementsFactory = project.getElementsFactory();
		Region region = elementsFactory.createRegionInstance();
		
		//Region must be a child of a state machine
		if(owner != null) {
			region.setOwner(owner);
		}
		
		return region;
	}
	
	public org.w3c.dom.Element createBaseXML(Element element, Document xmlDoc) {
		org.w3c.dom.Element data = xmlDoc.createElement("data");
		
		//Add attributes
		org.w3c.dom.Element attributes = xmlDoc.createElement("attributes");
		
		//Add Name
		if(!name.equals("") && !name.equals(null)) {
			org.w3c.dom.Element name = xmlDoc.createElement("name");
			name.appendChild(xmlDoc.createTextNode(this.name));
			attributes.appendChild(name);
		} else {
			org.w3c.dom.Element name = xmlDoc.createElement("name");
			attributes.appendChild(name);
		}
		data.appendChild(attributes);
		
		//Get stereotypes
		List<Stereotype> stereotypes = StereotypesHelper.getStereotypes(element);
		for(Stereotype stereotype : stereotypes) {
			CameoUtils.logGUI("Found stereotype : " + stereotype.getName());
			if(stereotype.getName() != "" && stereotype.getName() != null) {
				com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package profile = StereotypesHelper.getProfileForStereotype(stereotype);
				String profileName = profile.getName();
				
				org.w3c.dom.Element xmlStereotype = xmlDoc.createElement("stereotype");
				xmlStereotype.setAttribute("profile", profileName);
				xmlStereotype.setAttribute("profileId",  profile.getLocalID());
				xmlStereotype.appendChild(xmlDoc.createTextNode(stereotype.getName()));
				attributes.appendChild(xmlStereotype);
			}
			
		}
		//Add ID
		org.w3c.dom.Element id = xmlDoc.createElement("id");
		org.w3c.dom.Element cameoID = xmlDoc.createElement("cameo");
		cameoID.appendChild(xmlDoc.createTextNode(element.getLocalID()));
		id.appendChild(cameoID);
		data.appendChild(id);
		
		//Add parent relationship
		org.w3c.dom.Element relationship = xmlDoc.createElement("relationships");
		
		if(element.getOwner() != null) {
			org.w3c.dom.Element hasParent = xmlDoc.createElement("hasParent");
			Element parent = null;
			if((element instanceof Pseudostate) || (element instanceof com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State)) {
				Element region = element.getOwner();
				parent = region.getOwner();
			} else {
				parent = element.getOwner();
			}
			hasParent.appendChild(xmlDoc.createTextNode(parent.getLocalID()));
			relationship.appendChild(hasParent);
		}
		data.appendChild(relationship);
		
		return data;
	}
	
	public Element createNestedPorts(Project project, Element owner) {
		Element sysmlPackage = CameoUtils.findNearestPackage(project, owner);
		Profile sysmlProfile = StereotypesHelper.getProfile(project, "SysML"); 
		Stereotype blockStereotype = StereotypesHelper.getStereotype(project, "Block", sysmlProfile);
		
		Element block = createClassWithStereotype(project, owner.getHumanName().replace("Port ", ""), blockStereotype, sysmlPackage);
		return block;
	}
	
	public Element createNestedProperties(Project project, Element owner) {
		Element sysmlPackage = CameoUtils.findNearestPackage(project, owner);
		Profile sysmlProfile = StereotypesHelper.getProfile(project, "SysML"); 
		Stereotype blockStereotype = StereotypesHelper.getStereotype(project, "Block", sysmlProfile);
		
		Element block = createClassWithStereotype(project, name, blockStereotype, sysmlPackage);
		return block;
	}
	
	public org.w3c.dom.Element getAttributes(NodeList dataNodes) {
		org.w3c.dom.Element attributes = null;
		for(int i = 0; i < dataNodes.getLength(); i++) {
			Node dataNode = dataNodes.item(i);
			if(dataNode.getNodeType() == Node.ELEMENT_NODE) {
				if(dataNode.getNodeName().equals(XmlTagConstants.ATTRIBUTES)) {
					attributes = (org.w3c.dom.Element) dataNode;
				}
			}
		}
		return attributes;
	}
	
	public org.w3c.dom.Element getRelationships(NodeList dataNodes) {
		org.w3c.dom.Element attributes = null;
		for(int i = 0; i < dataNodes.getLength(); i++) {
			Node dataNode = dataNodes.item(i);
			if(dataNode.getNodeType() == Node.ELEMENT_NODE) {
				if(dataNode.getNodeName().equals(XmlTagConstants.RELATIONSHIPS)) {
					attributes = (org.w3c.dom.Element) dataNode;
				}
			}
		}
		return attributes;
	}
	
	public boolean isTyped(Element element) {
		TypedElement elementTyped = (TypedElement)element;
		Type type = elementTyped.getType();
		if(type == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public org.w3c.dom.Element getType(NodeList dataNodes, String type) {
		org.w3c.dom.Element attributes = null;
		for(int i = 0; i < dataNodes.getLength(); i++) {
			Node dataNode = dataNodes.item(i);
			if(dataNode.getNodeType() == Node.ELEMENT_NODE) {
				if(dataNode.getNodeName().equals(type.toLowerCase())) {
					attributes = (org.w3c.dom.Element) dataNode;
				}
			}
		}
		return attributes;
	}
	
	protected String setInvalidParentMessage(Element owner) {
		try {
			invalidParentMessage = CommonElement.invalidParentRoot + sysmlConstant + " must be a child of " + requiredParentType + ".\n\tName = " + name + "; ID = " + EAID + "; Invalid Parent Type = " + owner.getHumanType();
		} catch(NullPointerException npe) {
			invalidParentMessage = CommonElement.invalidParentRoot + sysmlConstant + " must be a child of " + requiredParentType + ".\n\tName = " + name + "; ID = " + EAID + "; Invalid Parent Type = null";
		}
		return invalidParentMessage;
	}
}
