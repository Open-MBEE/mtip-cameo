package org.aero.huddle.ModelElements;

import org.aero.huddle.util.CameoUtils;
import org.aero.huddle.util.SysmlConstants;
import org.aero.huddle.util.XMLItem;
import org.aero.huddle.util.XmlTagConstants;
import org.w3c.dom.Document;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;
import com.nomagic.magicdraw.uml.symbols.paths.PathElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class Link extends CommonElement {
	public Link(String name, String EAID) {
		super(name, EAID);
		this.creationType = XmlTagConstants.ELEMENTSFACTORY;
		this.sysmlConstant = SysmlConstants.LINK;
		this.xmlConstant = XmlTagConstants.LINK;
		this.sysmlElement = f.createInstanceSpecificationInstance();
	}

	@Override
	public Element createElement(Project project, Element owner, XMLItem xmlElement) {
		
		return null;
	}
	
	public org.w3c.dom.Element writeToXML(Element element, Project project, Document xmlDoc, PresentationElement presentationElement) {
		org.w3c.dom.Element data = super.writeToXML(element, project, xmlDoc);
		org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());
		
		PathElement pathElement = (PathElement)presentationElement;
		
		Element supplier = pathElement.getSupplier().getElement();
		Element client = pathElement.getClient().getElement();
		
		if(supplier != null) {
			org.w3c.dom.Element supplierRel = createRel(xmlDoc, supplier, XmlTagConstants.SUPPLIER);
			relationships.appendChild(supplierRel);
		} else {
			CameoUtils.logGUI("No supplier element found for link.\n");
		}
		if(client != null) {
			org.w3c.dom.Element clientRel = createRel(xmlDoc, client, XmlTagConstants.CLIENT);
			relationships.appendChild(clientRel);
		} else {
			CameoUtils.logGUI("No client element found link.\n");
		}
		
		return data;
	}
}