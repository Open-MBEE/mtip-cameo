/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.sysml.internalblock;

import org.aero.mtip.metamodel.core.CommonElement;
import org.aero.mtip.util.ElementData;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class RequiredInterface extends CommonElement {

	public RequiredInterface(String name, String importId) {
		super(name, importId);
	}

	@Override
	public Element createElement(Project project, Element owner, ElementData xmlElement) {
		return null;
	}
}
