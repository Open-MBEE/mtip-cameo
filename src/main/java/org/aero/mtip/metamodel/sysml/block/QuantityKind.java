/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */
package org.aero.mtip.metamodel.sysml.block;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.profiles.MDCustomizationForSysML;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.uml.Finder;

public class QuantityKind extends InstanceSpecification {

	public QuantityKind(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = SysmlConstants.QUANTITY_KIND;
		this.xmlConstant = XmlTagConstants.QUANTITY_KIND;
		this.creationStereotype = MDCustomizationForSysML.getQuantityKindStereotype();
	}

	protected void setClassifier() {
		this.classifier = (com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element)Finder.byQualifiedName().find(Application.getInstance().getProject(), "SysML::Libraries::UnitAndQuantityKind::QuantityKind");
	}
}
