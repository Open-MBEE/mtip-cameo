package org.aero.huddle.ModelElements.Activity;

import java.util.Map;

import org.aero.huddle.ModelElements.CommonElement;
import org.aero.huddle.XML.Import.ImportXmlSysml;
import org.aero.huddle.util.CameoUtils;
import org.aero.huddle.util.XMLItem;
import org.aero.huddle.util.XmlTagConstants;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public abstract class ActivityNode extends CommonElement {

	public ActivityNode(String name, String EAID) {
		super(name, EAID);
	}
	
	@Override
	public void createDependentElements(Project project, Map<String, XMLItem> parsedXML, XMLItem modelElement) {
		if(modelElement.hasAttribute(XmlTagConstants.ATTRIBUTE_NAME_ACTIVITY)) {
			String activityId = modelElement.getAttribute(XmlTagConstants.ATTRIBUTE_NAME_ACTIVITY);
			ImportXmlSysml.buildElement(project, parsedXML, parsedXML.get(activityId), activityId);
		}
	}
	
	
	@Override
	public void setOwner(Project project, Element owner) {
		if(!(owner instanceof Activity)) {
			owner = CameoUtils.findNearestActivity(project, owner);
		}
		sysmlElement.setOwner(owner);
	}
}
