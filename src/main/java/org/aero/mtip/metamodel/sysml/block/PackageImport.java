package org.aero.mtip.metamodel.sysml.block;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonRelationship;

public class PackageImport extends CommonRelationship {

	public PackageImport(String name, String importId) {
		super(name, importId);
		this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
		this.metamodelConstant = SysmlConstants.PACKAGE_IMPORT;
		this.xmlConstant = XmlTagConstants.PACKAGE_IMPORT;
		this.element = f.createPackageImportInstance();
	}
}
