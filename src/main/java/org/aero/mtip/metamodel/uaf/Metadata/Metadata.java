package org.aero.mtip.metamodel.uaf.Metadata;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.general.Comment;

public class Metadata extends Comment {

	public Metadata(String name, String importId) {
		super(name, importId);
		this.metamodelConstant = UAFConstants.METADATA;
		this.xmlConstant = XmlTagConstants.METADATA;
	}
}