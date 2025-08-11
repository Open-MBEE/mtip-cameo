package org.aero.mtip.metamodel.uaf;

import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonElement;

public class ArchitecturalDescription extends CommonElement {

    public ArchitecturalDescription(String name, String EAID) {
        super(name, EAID);
        this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
        this.metamodelConstant = UAFConstants.ARCHITECTURAL_DESCRIPTION;
        this.xmlConstant = XmlTagConstants.ARCHITECTURAL_DESCRIPTION;
        this.element = f.createModelInstance();
    }
}
