package org.aero.mtip.XML;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.CheckForNull;
import org.aero.mtip.constants.XmlTagConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils {
  public static Iterable<Node> iterable(final NodeList nodeList) {
    return () -> new Iterator<Node>() {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < nodeList.getLength();
        }

        @Override
        public Node next() {
            if (!hasNext()) {
              throw new NoSuchElementException();
            }
                
            return nodeList.item(index++); 
        }
    };
  }
  
  public static List<Element> asElementList(NodeList nodeList) {
    return StreamSupport.stream(XmlUtils.iterable(nodeList).spliterator(), false)
        .filter(x -> x instanceof Element)
        .map(x -> (Element)x)
        .collect(Collectors.toList());
  }
  
  public static List<Element> asElementListWithTag(NodeList nodeList, String tagName) {
    return StreamSupport.stream(XmlUtils.iterable(nodeList).spliterator(), false)
        .filter(x -> x.getNodeName().equals(tagName) && x instanceof Element)
        .map(x -> (Element)x)
        .collect(Collectors.toList());
  }
  
  @CheckForNull
  public static Element getFirstChildByTagName(Node node, String tagName) {
    return StreamSupport.stream(XmlUtils.iterable(node.getChildNodes()).spliterator(), false)
        .filter(x -> x.getNodeName().equals(tagName))
        .map(x -> (Element)x)
        .findFirst()
        .orElse(null);
  }
  
  @CheckForNull
  public static Element getFirstChildByKey(Node node, String key) {
    for (Node childNode : XmlUtils.asElementList(node.getChildNodes())) {
      if (!getKey(childNode).equals(key)) {
        continue;
      }
      
      return (Element)childNode;
    }
    
    return null;
  }
  
  /**
   * Returns value for the _dtype attribute of the given node if it exists. Otherwise, empty string.
   * @param node Node to check for the data type attribute
   * @return String value in attribute _dtype of the given node
   */
  public static String getDataType(Node node) {
    if (!(node instanceof Element)) {
      return "";
    }
    
    return ((Element)node).getAttribute(XmlTagConstants.ATTRIBUTE_DATA_TYPE);
  }
  
  public static String getKey(Node node) {
    if (!(node instanceof Element)) {
      return "";
    }
    
    return ((Element)node).getAttribute(XmlTagConstants.ATTRIBUTE_KEY);
  }
  
  public static boolean isDataTypeList(Node node) {
    if (!XmlUtils.getDataType(node).equals(XmlTagConstants.ATTRIBUTE_TYPE_LIST)) {
      return false;
    }
    
    return true;
  }
  
  public static boolean isStereotypeTaggedValueAttribute(Node node) {
    if (!getKey(node).equals(XmlTagConstants.STEREOTYPE_TAGGED_VALUE)) {
      return false;
    }
    
    return true;   
  }
  
  public static boolean isStereotypeAttribute(Node node) {
    if (!getKey(node).equals(XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE)) {
      return false;
    }
    
    return true;    
  }
}
