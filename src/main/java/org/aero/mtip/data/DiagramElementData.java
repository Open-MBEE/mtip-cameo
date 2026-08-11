package org.aero.mtip.data;

import java.awt.Rectangle;

public class DiagramElementData {
  private String id = "";
  private String type = "";
  private String orientation = "";
  private Rectangle bounds = null;

  public DiagramElementData() {
    
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Rectangle getBounds() {
    return bounds;
  }

  public void setBounds(Rectangle bounds) {
    this.bounds = bounds;
  }

  public String getOrientation() {
    return orientation;
  }

  public void setOrientation(String orientation) {
    this.orientation = orientation;
  }
  
  @Override
  public String toString() {
    return String.format("Type=%s; Id=%s; Bounds=%s", getType(), getId(), getBounds().toString());
  }
}
