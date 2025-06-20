/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */
package org.aero.mtip.metamodel.core.matrix;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.util.XMLItem;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.dependencymatrix.diagram.DependencyMatrixDiagramDescriptor;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class DependencyMatrix extends AbstractMatrix {
	

	public DependencyMatrix(String name, String EAID) {
		super(name, EAID);
		this.metamodelConstant = SysmlConstants.DEPENDENCY_MATRIX;
		this.xmlConstant = XmlTagConstants.DEPENDENCY_MATRIX;
		this.cameoConstant = DependencyMatrixDiagramDescriptor.DEFAULT_DEPENDENCY_MATRIX_DIAGRAM_TYPE;
	}
		
	@Override
	public Element createElement(Project project, Element owner, XMLItem xmlElement) {
		return super.createElement(project, owner, xmlElement);
	}	
	
	@Override
	public org.w3c.dom.Element writeToXML(Element element) {
		org.w3c.dom.Element data = super.writeToXML(element);

		// get attributes part
		org.w3c.dom.Element attributes = getAttributes(data.getChildNodes());
		org.w3c.dom.Element relationships = getType(data.getChildNodes(), XmlTagConstants.RELATIONSHIPS);
		
		//MatrixFilter
		Profile dependencyMatrixProfile = StereotypesHelper.getProfile(project, "Dependency Matrix Profile");
		Stereotype dependencyMatrixStereotype = StereotypesHelper.getStereotype(project, "DependencyMatrix", dependencyMatrixProfile);
		Stereotype matrixFilterStereotype = StereotypesHelper.getStereotype(project, "MatrixFilter", dependencyMatrixProfile);
		
		// 2021x Refactor to TaggedValues
		// Find and write row scope to XML
//
//		Property rowScopeProperty = StereotypesHelper.getPropertyByName(matrixFilterStereotype, "rowScope");
//		Slot rowScopeSlot = StereotypesHelper.getSlot(element, rowScopeProperty, false);
//		try {
//			// Try Casting to LinkedHastSet<Element>
//			Element rowScopeElement = (Element) InstanceSpecificationHelper.getValueBySlot(rowScopeSlot);
//			
//			if(rowScopeElement != null) {
//				org.w3c.dom.Element rowScopeTag = XmlWriter.createMtipRelationship(rowScopeElement, XmlTagConstants.ROW_SCOPE);
//				XmlWriter.add(relationships, rowScopeTag);
//			}
//		}catch (ClassCastException cce) {
//			ImportLog.log("Error casting row scope element to Element class for dependency matrix named: " + this.name);
//		}

//		if(dependencyCriteriaXMLStr != null) {
//			org.w3c.dom.Element rowScope = createRel(xmlDoc, dependencycriteriaXMLStr, XmlTagConstants.ROW_SCOPE);
//			relationships.appendChild(rowScope);
//		}

//		Element rowElementTypeElement = (Element) InstanceSpecificationHelper.getValueBySlot(rowElementTypeSlot);
		
//		org.w3c.dom.Element nameTag = createStringAttribute(xmlDoc, XmlTagConstants.COLUMN_ELEMENT_TYPE, columnElementTypeStr);
//		org.w3c.dom.Element columnElementType = xmlDoc.createElement();
//		org.w3c.dom.Element rowElementType = xmlDoc.createElement(XmlTagConstants.ROW_ELEMENT_TYPE);
//		
//		// Find and write column scope to XML
//		try {
//			Property columnScopeProperty = StereotypesHelper.getPropertyByName(matrixFilterStereotype, "columnScope");
//			Slot columnScopeSlot = StereotypesHelper.getSlot(element, columnScopeProperty, false);
//			Element columnScopeElement = (Element) InstanceSpecificationHelper.getValueBySlot(columnScopeSlot);
//			
//			if(columnScopeElement != null) {
//				org.w3c.dom.Element columnScopeTag = XmlWriter.createMtipRelationship(columnScopeElement, XmlTagConstants.COLUMN_SCOPE);
//				XmlWriter.add(relationships, columnScopeTag);
//			}
//		}catch (ClassCastException cce) {
//			ImportLog.log("Error casting column scope element to Element class for dependency matrix named: " + this.name);
//		}
//		
//		// Find and write dependency Criteria to XML
//		Property dependencyCriteriaProperty = StereotypesHelper.getPropertyByName(dependencyMatrixStereotype, "dependencyCriteria");
//		Slot dependencyCriteriaSlot = StereotypesHelper.getSlot(element, dependencyCriteriaProperty, false);
//		try {
//			ArrayList<String> dependencyCriteriaXMLStr = (ArrayList<String>) InstanceSpecificationHelper.getValueBySlot(dependencyCriteriaSlot);
//			CameoUtils.logGUI("Dependency criteria XML: " + dependencyCriteriaXMLStr);
//		} catch(ClassCastException cce) {
//			ImportLog.log("Error casting dependency criteria XML.");
//		}
//		
//		
////		if(dependencyCriteriaXMLStr != null) {
////			org.w3c.dom.Element rowScope = createRel(xmlDoc, dependencycriteriaXMLStr, XmlTagConstants.ROW_SCOPE);
////			relationships.appendChild(rowScope);
////		}
//		
////		Element rowElementTypeElement = (Element) InstanceSpecificationHelper.getValueBySlot(rowElementTypeSlot);
//		
////		org.w3c.dom.Element nameTag = createStringAttribute(xmlDoc, XmlTagConstants.COLUMN_ELEMENT_TYPE, columnElementTypeStr);
////		org.w3c.dom.Element columnElementType = xmlDoc.createElement();
////		org.w3c.dom.Element rowElementType = xmlDoc.createElement(XmlTagConstants.ROW_ELEMENT_TYPE);
////		
//		// Find and write column scope to XML
////		try {
////			Property columnScopeProperty = StereotypesHelper.getPropertyByName(matrixFilterStereotype, "columnScope");
////			Slot columnScopeSlot = StereotypesHelper.getSlot(element, columnScopeProperty, false);
////			Element columnScopeElement = (Element) InstanceSpecificationHelper.getValueBySlot(columnScopeSlot);
////			
////			if(columnScopeElement != null) {
////				org.w3c.dom.Element columnScope = createRel(xmlDoc, columnScopeElement, XmlTagConstants.COLUMN_SCOPE);
////				relationships.appendChild(columnScope);
////			}
////		}catch (ClassCastException cce) {
////			ImportLog.log("Error casting column scope element to Element class for dependency matrix named: " + this.name);
////		}
////		
////		// Find and write dependency Criteria to XML
////		Property dependencyCriteriaProperty = StereotypesHelper.getPropertyByName(dependencyMatrixStereotype, "dependencyCriteria");
////		Slot dependencyCriteriaSlot = StereotypesHelper.getSlot(element, dependencyCriteriaProperty, false);
////		try {
////			ArrayList<String> dependencyCriteriaXMLStr = (ArrayList<String>) InstanceSpecificationHelper.getValueBySlot(dependencyCriteriaSlot);
////			CameoUtils.logGUI("Dependency criteria XML: " + dependencyCriteriaXMLStr);
////		} catch(ClassCastException cce) {
////			ImportLog.log("Error casting dependency criteria XML.");
////		}
////		
////		
//////		if(dependencyCriteriaXMLStr != null) {
//////			org.w3c.dom.Element rowScope = createRel(xmlDoc, dependencycriteriaXMLStr, XmlTagConstants.ROW_SCOPE);
//////			relationships.appendChild(rowScope);
//////		}
////		
////		Property rowElementTypeProperty = StereotypesHelper.getPropertyByName(dependencyMatrixStereotype, "rowElementType");
////		try {
////			Slot rowElementTypeSlot = StereotypesHelper.getSlot(element, rowElementTypeProperty, false);
////		} catch (NullPointerException npe) {
////			CameoUtils.logGUI("No rowElementType has been selected for depedency matrix with id: " + element.getLocalID());
////		}
////		
//////		Element rowElementTypeElement = (Element) InstanceSpecificationHelper.getValueBySlot(rowElementTypeSlot);
////		
//////		org.w3c.dom.Element nameTag = createStringAttribute(xmlDoc, XmlTagConstants.COLUMN_ELEMENT_TYPE, columnElementTypeStr);
//////		org.w3c.dom.Element columnElementType = xmlDoc.createElement();
//////		org.w3c.dom.Element rowElementType = xmlDoc.createElement(XmlTagConstants.ROW_ELEMENT_TYPE);
//////		
//////		//DependencyMatrix
//////		org.w3c.dom.Element dependencyCrtieraia = xmlDoc.createElement(XmlTagConstants.DEPENDENCY_CRITERIA);
//////		
////		
//////		
//////		org.w3c.dom.Element elementListTag = xmlDoc.createElement(XmlTagConstants.ELEMENT);
//////		elementListTag.setAttribute(XmlTagConstants.ATTRIBUTE_DATA_TYPE, XmlTagConstants.ATTRIBUTE_TYPE_LIST);
//////		
//////		
//////		org.w3c.dom.Element relationshipListTag = xmlDoc.createElement(XmlTagConstants.RELATIONSHIP);
//////		relationshipListTag.setAttribute(XmlTagConstants.ATTRIBUTE_DATA_TYPE, XmlTagConstants.ATTRIBUTE_TYPE_LIST);
//////		
//////		writeElements(xmlDoc, project, elementListTag, relationshipListTag, element);
//////		
//////		relationships.appendChild(elementListTag);
//////		relationships.appendChild(relationshipListTag);
		return data;
	}
	

	@Override
	public String getCameoDiagramConstant() {
		return "Dependency Matrix";
	}

	@Override
	public String getDiagramType() {
		return this.xmlConstant;
	}
}
