/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.core;

import java.util.Collection;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DirectedRelationship;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public abstract class CommonDirectedRelationship extends CommonRelationship {

	public CommonDirectedRelationship(String name, String EAID) {
		super(name, EAID);
	}
	
	public Element getSupplier() {
		Collection<Element> sources = ((DirectedRelationship)element).getSource();
		return sources.iterator().next();
	}
	
	public Element getClient() {
		Collection<Element> targets = ((DirectedRelationship)element).getTarget();
		return targets.iterator().next();
	}
	
	public void setSupplier(Element supplier) {
	  DirectedRelationship directedRelationship = (DirectedRelationship)element;
      directedRelationship.getSource().add(supplier);
	}
	
	public void setClient(Element client) {
	  DirectedRelationship directedRelationship = (DirectedRelationship)element;
      directedRelationship.getTarget().add(client);
	}
}
