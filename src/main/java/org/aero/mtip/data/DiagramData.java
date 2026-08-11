package org.aero.mtip.data;

import java.util.ArrayList;
import java.util.List;

public class DiagramData extends ElementData {
  private List<DiagramElementData> diagramElementDatas = new ArrayList<DiagramElementData> ();
  private List<DiagramConnectorData> diagramConnectorDatas = new ArrayList<DiagramConnectorData> ();
  
  public DiagramData() {

  }
  
  public void addDiagramElementData(DiagramElementData diagramElementData) {
    diagramElementDatas.add(diagramElementData);
  }
  
  public void addDiagramConnectorData(DiagramConnectorData diagramConnectorData) {
    diagramConnectorDatas.add(diagramConnectorData);
  }

  public List<DiagramElementData> getDiagramElementDatas() {
    return diagramElementDatas;
  }

  public List<DiagramConnectorData> getDiagramConnectorDatas() {
    return diagramConnectorDatas;
  }
}
