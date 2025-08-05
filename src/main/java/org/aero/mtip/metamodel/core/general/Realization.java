/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.core.general;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.metamodel.core.CommonDirectedRelationship;

public class Realization extends CommonDirectedRelationship {

    public Realization(String name, String EAID) {
        super(name, EAID);
        this.creationType = XmlTagConstants.ELEMENTS_FACTORY;
        this.metamodelConstant = SysmlConstants.REALIZATION;
        this.xmlConstant = XmlTagConstants.REALIZATION;
        this.element = f.createRealizationInstance();
    }
}
