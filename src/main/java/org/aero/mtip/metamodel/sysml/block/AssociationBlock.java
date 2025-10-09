/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.block;

import java.util.HashMap;
import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.profiles.SysML;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.MtipUtils;
import org.aero.mtip.util.XMLItem;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class AssociationBlock extends CommonElement {
	protected Element supplier;
	protected Element client;
	
	public AssociationBlock(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.ASSOCIATION_BLOCK;
		this.xmlConstant = XmlTagConstants.ASSOCIATION_BLOCK;
		this.element = f.createAssociationClassInstance();
		this.creationStereotype = SysML.getBlockStereotype();
	}

	@Override
	public Element createElement(Project project, Element owner, XMLItem xmlElement) {
		AssociationClass associationClass = (AssociationClass) super.createElement(project, owner, xmlElement);
		
		if(xmlElement.hasSupplierElement()) {
			this.supplier = xmlElement.getSupplierElement();
		}
		if(xmlElement.hasClientElement()) {
			this.client = xmlElement.getClientElement();
		}
		
		// Owning package must be package where client and supplier are located. EA Imports have no hasParent.
		// Owner will default to main model but needs to be the lower level package.
		if(supplier != null) {
			if(owner.equals(project.getPrimaryModel())) {
				owner = CameoUtils.findNearestPackage(project, supplier);
			}
		}
		
		if(client != null && supplier != null) {
			ModelHelper.setSupplierElement(associationClass, supplier);
			ModelHelper.setClientElement(associationClass, client);
			ModelHelper.setNavigable(ModelHelper.getFirstMemberEnd(associationClass), true);
			ModelHelper.setNavigable(ModelHelper.getSecondMemberEnd(associationClass), true);
		} else {
			Logger.log(String.format("Supplier or client was not set. Association block %s not created.", xmlElement.getName()));
		}
		
		StereotypesHelper.addStereotype(associationClass, SysML.getBlockStereotype());

		return (Element)associationClass;
	}
	
	public void createDependentElements(HashMap<String, XMLItem> parsedXML, XMLItem modelElement) {
		createClient(project, modelElement, parsedXML);
		createSupplier(project, modelElement, parsedXML);
	}
	
	public void createClient(Project project, XMLItem modelElement, HashMap<String, XMLItem> parsedXML) {
		if (!modelElement.hasClient()) {
			Logger.log(String.format("No client found for association block with id %s.", importId));
			return;
		}
		
		String clientID = modelElement.getClient();
			
		if (!parsedXML.containsKey(clientID)) {
			Logger.log(String.format("No data tag found in XML for client id %s", clientID));
			return;
		}
		
		Element client = Importer.getInstance().buildElement(parsedXML, parsedXML.get(clientID));
		modelElement.setClientElement(client);
		modelElement.addAttribute("client", MtipUtils.getId(client));
	}
	
	public void createSupplier(Project project, XMLItem modelElement, HashMap<String, XMLItem> parsedXML) {
		if(!modelElement.hasSupplier()) {
			Logger.log(String.format("No supplier found for association block with id %s.", importId));
			return;
		}
		
		String supplierID = modelElement.getSupplier();
		
		
		if(!parsedXML.containsKey(supplierID)) {
			Logger.log(String.format("No data tag found in XML for supplier id %s", supplierID));
			return;
		}
		
		Element supplier = Importer.getInstance().buildElement(parsedXML, parsedXML.get(modelElement.getSupplier()));
		modelElement.setSupplierElement(supplier);
		modelElement.addAttribute("supplier",  MtipUtils.getId(supplier));
	}

	@Override
	public org.w3c.dom.Element writeToXML(Element element) {
		org.w3c.dom.Element data = super.writeToXML(element);
		org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());		
		
		writeSupplier(relationships, element);
		writeClient(relationships, element);
		
		return data;
	}
	
	public void writeSupplier(org.w3c.dom.Element relationships, Element element) {
		supplier = ModelHelper.getSupplierElement(element);
		
		if(supplier == null) {
			return;
		}
		
		org.w3c.dom.Element supplierTag = XmlWriter.createMtipRelationship(supplier, XmlTagConstants.SUPPLIER);
		XmlWriter.add(relationships, supplierTag);
	}
	
	public void writeClient(org.w3c.dom.Element relationships, Element element) {
		client = ModelHelper.getClientElement(element);
		
		if(client == null) {
			return;
		}
		
		org.w3c.dom.Element clientTag = XmlWriter.createMtipRelationship(client, XmlTagConstants.CLIENT);
		XmlWriter.add(relationships, clientTag);
	}
}
