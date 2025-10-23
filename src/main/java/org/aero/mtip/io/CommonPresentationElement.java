package org.aero.mtip.io;

import java.awt.Color;
import java.awt.Font;
import com.nomagic.magicdraw.properties.Property;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;



public class CommonPresentationElement {  
  private PresentationElement presentationElement;
  
  public CommonPresentationElement(PresentationElement presentationElement) {
    this.presentationElement = presentationElement;
  }
  
  public Color getFillColor() {
    Property prop = presentationElement.getPropertyManager().getProperty(PresentationElementProperties.FILL_COLOR);
    
    if (prop == null) {
      return null;
    }
    
    Object obj = prop.getValue();
    
    if (!(obj instanceof Color)) {
      return null;
    }
    
    return (Color)obj;
  }  
  
  public Font getFont() {
     Property prop = presentationElement.getPropertyManager().getProperty(PresentationElementProperties.FILL_COLOR);
    
    if (prop == null) {
      return null;
    }
    
    Object obj = prop.getValue();
    
    if (!(obj instanceof Color)) {
      return null;
    }
    
    return (Font)obj;
  }
  
  public Color getPencolor() {
    Property prop = presentationElement.getPropertyManager().getProperty(PresentationElementProperties.PEN_COLOR);
    
    if (prop == null) {
      return null;
    }
    
    Object obj = prop.getValue();
    
    if (!(obj instanceof Color)) {
      return null;
    }
    
    return (Color)obj;
  }
  
  public Color getTextColor() {
    Property prop = presentationElement.getPropertyManager().getProperty(PresentationElementProperties.TEXT_COLOR);
    
    if (prop == null) {
      return null;
    }
    
    Object obj = prop.getValue();
    
    if (!(obj instanceof Color)) {
      return null;
    }
    
    return (Color)obj;
  }
  
  public boolean getUseFillColor() {
    Property prop = presentationElement.getPropertyManager().getProperty(PresentationElementProperties.USE_FILL_COLOR);
    
    if (prop == null) {
      return false;
    }
    
    Object obj = prop.getValue();
    
    if (!(obj instanceof Boolean)) {
      return false;
    }
    
    return (Boolean)obj;
  }
}
