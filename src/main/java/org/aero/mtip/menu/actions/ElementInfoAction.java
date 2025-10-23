/*
 * The Aerospace Corporation Huddle_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */
package org.aero.mtip.menu.actions;

import java.awt.event.ActionEvent;
import java.util.List;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.MtipUtils;
import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class ElementInfoAction extends MDAction {
  public static final String NAME = "Show Element Info";
  private static final long serialVersionUID = 8068907130520308356L;

  private Element element = null;

  public ElementInfoAction(String id, String name, Element element) {
    super(id, name, null, null);

    this.element = element;
  }

  public void actionPerformed(ActionEvent e) {
    if (element == null) {
      return;
    }
    
    List<Stereotype> stereotypes = StereotypesHelper.getStereotypes(element);
    String stereotypeInfo = "Stereotypes:";
    
    for (Stereotype stereotype : stereotypes) {
      stereotypeInfo += String.format("\t\n%s", stereotype.getName());
    }

    CameoUtils.logGui(String.format("Element type: %s\n%s", MtipUtils.getEntityType(element), stereotypeInfo));
  }
}
