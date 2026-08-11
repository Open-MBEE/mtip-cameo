package org.aero.mtip.menu.actions;

import java.awt.event.ActionEvent;
import org.aero.mtip.util.CameoUtils;
import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.magicdraw.uml.symbols.shapes.DiagramFrameView;

public class InspectDiagramPresentationElementAction extends MDAction {
  private static final long serialVersionUID = 9081506268061281281L;
  private DiagramPresentationElement diagramPresentationElement;
  
  public InspectDiagramPresentationElementAction(String id, String name, DiagramPresentationElement diagramPresentationElement) {
    super(id, name, null, null);
    
    this.diagramPresentationElement = diagramPresentationElement;
  }
  
  public void actionPerformed(ActionEvent e) {    
    DiagramFrameView dfv = diagramPresentationElement.getDiagramFrame();
    CameoUtils.logGui(dfv.toString());
  }
}
