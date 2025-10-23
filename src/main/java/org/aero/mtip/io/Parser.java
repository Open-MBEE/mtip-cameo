package org.aero.mtip.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.xml.parsers.ParserConfigurationException;
import org.aero.mtip.XML.XmlParser;
import org.aero.mtip.util.ElementData;
import org.xml.sax.SAXException;
import com.nomagic.magicdraw.core.Project;

public abstract class Parser {
  protected HashMap<String, ElementData> allElementData = new HashMap<String, ElementData>();
  protected HashMap<String, ElementData> stereotypeElementData = new HashMap<String, ElementData>();
  protected HashMap<String, ElementData> profileElementData = new HashMap<String, ElementData>();
  
  protected List<ElementData> properties = new ArrayList<ElementData>();
  
  public Parser() {
  }
  
  public abstract void parse(File file) throws ParserConfigurationException, SAXException, IOException;
  
  @CheckForNull
  public static Parser getParserForFile(File file, Project project) {
    String extension = getExtension(file);
    
    if (extension.equals("xml")) {
      return new XmlParser();
    }
    
    return null;
  }
  
  /**
   * Finds and returns the extension of the given file as lower case string
   * (i.e. xml, json, txt)
   * @param file File to find the extension
   * @return File extension as lower case string. If no extension, empty string.
   */
  private static String getExtension(File file) {
    String name = file.getName();
    
    int lastIndexOf = name.lastIndexOf(".");
    
    if (lastIndexOf == -1) {
        return ""; // empty extension
    }
    
    return name.substring(lastIndexOf + 1).toLowerCase();
  }

  public HashMap<String, ElementData> getAllElementData() {
    return allElementData;
  }

  public HashMap<String, ElementData> getStereotypeElementData() {
    return stereotypeElementData;
  }

  public HashMap<String, ElementData> getProfileElementData() {
    return profileElementData;
  }

  public List<ElementData> getProperties() {
    return properties;
  }
}
