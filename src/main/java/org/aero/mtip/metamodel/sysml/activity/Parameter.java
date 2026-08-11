package org.aero.mtip.metamodel.sysml.activity;

import javax.annotation.CheckForNull;

import org.aero.mtip.XML.XmlWriter;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.data.ElementData;
import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralBoolean;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ParameterDirectionKind;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ParameterDirectionKindEnum;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

public class Parameter extends CommonElement {
	public Parameter(String name, String importId) {
		super(name, importId);

		creationType = XmlTagConstants.ELEMENTS_FACTORY;
		metamodelConstant = SysmlConstants.PARAMETER;
		xmlConstant = XmlTagConstants.SYSML_PARAMETER;
		element = f.createParameterInstance();
	}

	public Element createElement(Project project, Element owner, ElementData xmlElement) {
		super.createElement(project, owner, xmlElement);

		setDirection();
		setDefaultValue();

		return element;
	}

	private void setDirection() {
		if (!elementData.hasAttribute(XmlTagConstants.ATTRIBUTE_KEY_DIRECTION)) {
			return;
		}

		asParameter().setDirection(
				ParameterDirectionKindEnum.get(elementData.getAttribute(XmlTagConstants.ATTRIBUTE_KEY_DIRECTION)));
	}

	private void setDefaultValue() {
		if (!elementData.hasAttribute(XmlTagConstants.ATTRIBUTE_KEY_DEFAULT_VALUE)) {
			return;
		}
		
		if (asParameter().getType() == null) {
			Logger.log("Property type is null. Cannot set default value.");
			return;
		}

		String defaultValue = elementData.getAttribute(XmlTagConstants.ATTRIBUTE_KEY_DEFAULT_VALUE);

		try {
			if (elementData.getAttribute(XmlTagConstants.TYPED_BY).contentEquals(SysmlConstants.BOOLEAN)) {
				boolean boolVal = Boolean.valueOf(defaultValue);
				LiteralBoolean valueSpecification = (LiteralBoolean) ModelHelper.createValueSpecification(project,
						asParameter().getType(), boolVal, null);
				asParameter().setDefaultValue(valueSpecification);
			} else if (elementData.getAttribute(XmlTagConstants.TYPED_BY).contentEquals(SysmlConstants.INTEGER)) {
				int intVal = Integer.parseInt(defaultValue);
				ValueSpecification valueSpecification = ModelHelper.createValueSpecification(project,
						asParameter().getType(), intVal, null);
				asParameter().setDefaultValue(valueSpecification);
			} else if (elementData.getAttribute(XmlTagConstants.TYPED_BY).contentEquals(SysmlConstants.REAL)) {
				double realVal = Double.parseDouble(defaultValue);
				LiteralReal valueSpecification = (LiteralReal) ModelHelper.createValueSpecification(project,
						asParameter().getType(), realVal, null);
				valueSpecification.setValue(realVal);
				asParameter().setDefaultValue(valueSpecification);
			} else if (elementData.getAttribute(XmlTagConstants.TYPED_BY).contentEquals(SysmlConstants.STRING)) {
				LiteralString valueSpecification = (LiteralString) ModelHelper.createValueSpecification(project,
						asParameter().getType(), defaultValue, null);
				asParameter().setDefaultValue(valueSpecification);
			} else {
				CameoUtils.logGui(String.format("Primitive type not recognized: %s",
						elementData.getAttribute(XmlTagConstants.TYPED_BY)));
			}
		} catch (Exception exception) {
			Logger.log(
					String.format("Error assigning default value to property with id: %s see stack trace: ", importId));
			Logger.logException(exception);
		}
	}

	public org.w3c.dom.Element writeToXML(Element element) {
		org.w3c.dom.Element data = super.writeToXML(element);
		org.w3c.dom.Element attributes = getAttributes(data.getChildNodes());
		org.w3c.dom.Element relationships = getRelationships(data.getChildNodes());

		writeDirection(attributes, element);
		writeDefaultValue(relationships, element);

		return data;
	}

	public void writeDirection(org.w3c.dom.Element attributes, Element element) {
		com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter parameter = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter) element;
		ParameterDirectionKind dk = parameter.getDirection();

		org.w3c.dom.Element directionTag = XmlWriter.createMtipStringAttribute(XmlTagConstants.ATTRIBUTE_KEY_DIRECTION,
				dk.toString());
		XmlWriter.add(attributes, directionTag);
	}

	public void writeDefaultValue(org.w3c.dom.Element relationships, Element element) {
		com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter parameter = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter) element;
		ValueSpecification vs = parameter.getDefaultValue();

		if (vs == null) {
			return;
		}

		org.w3c.dom.Element defaultValueTag = XmlWriter.createDefaultValueTag(vs);

		if (defaultValueTag == null) {
			return;
		}

		XmlWriter.add(relationships, defaultValueTag);
	}

	@CheckForNull
	public com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter asParameter() {
		return (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter) element;
	}
}
