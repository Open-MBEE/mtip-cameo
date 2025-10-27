/*
 * The Aerospace Corporation Huddle_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */

package org.aero.mtip.menu.actions;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import org.aero.mtip.util.CameoUtils;
import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.magicdraw.uml.actions.SelectInBrowserTreeUtils;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class FindElementByIdAction extends MDAction {
  private static final long serialVersionUID = 5553545596531198224L;

  public FindElementByIdAction(String id, String name) {
    super(id, name, null, null);
  }

  public void actionPerformed(ActionEvent e) {
    String elementId = JOptionPane.showInputDialog(MDDialogParentProvider.getProvider().getDialogOwner(), "Element id:", "Find Element by Id",
        JOptionPane.QUESTION_MESSAGE);
    
    if (elementId == null) {
      CameoUtils.logGui("No element id provided.");
      return;
    }
    
    Element element = (Element) Application.getInstance().getProject().getElementByID(elementId);
    
    if (element == null) {
      CameoUtils.logGui(String.format("No elemnet found by id: %s", elementId));
      return;
    }
    
    SelectInBrowserTreeUtils.selectInContainmentTree(element);
  }
}
