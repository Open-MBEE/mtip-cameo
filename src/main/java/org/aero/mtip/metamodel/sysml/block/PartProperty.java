/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.block;

import java.util.HashMap;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Importer;
import org.aero.mtip.profiles.MDCustomizationForSysML;
import org.aero.mtip.profiles.SysML;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.MtipUtils;
import org.aero.mtip.util.XMLItem;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class PartProperty extends org.aero.mtip.metamodel.sysml.sequence.Property {
	public PartProperty(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = SysmlConstants.PART_PROPERTY;
		this.xmlConstant = XmlTagConstants.PART_PROPERTY;
		this.element = f.createPropertyInstance();
	    this.creationStereotype = MDCustomizationForSysML.getPartPropertyStereotype();
	}
	
	@Override
	public void createDependentElements(HashMap<String, XMLItem> parsedXML, XMLItem modelElement) {		
		if(modelElement.hasAttribute(XmlTagConstants.TYPED_BY)) {
			String classifierID = modelElement.getAttribute(XmlTagConstants.TYPED_BY);
			try {
				Element type = Importer.getInstance().buildElement(parsedXML, parsedXML.get(classifierID));
				modelElement.addAttribute(XmlTagConstants.CLASSIFIER_TYPE, MtipUtils.getId(type));
			} catch (NullPointerException npe) {
				Logger.log("Failed to create/get typed by element for element with id" + this.importId);
			}
		}
	}
	
	@Override
	public void setOwner( Element owner) {
		if(owner == null) {
			String logMessage = "Owner is null. Could not add connector with id: " + this.importId + " to the model.";
			Logger.log(logMessage);
		}
		try {
			if(!(SysML.isBlock(owner))) {
				owner = CameoUtils.findNearestBlock(project, owner);
				if(owner == null) {
					String logMessage = "Invalid parent. Parent must be block " + name + " with id " + importId + ". No parents found in ancestors. Element could not be placed in model.";
					Logger.log(logMessage);

				}
				element.setOwner(owner);
			} else {
				element.setOwner(owner);
			}
		} catch(IllegalArgumentException iae) {
			String logMessage = "Invalid parent. Parent must be block " + name + " with id " + importId + ". Element could not be placed in model.";
			Logger.log(logMessage);
		}
	}
}