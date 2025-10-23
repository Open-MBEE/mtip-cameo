package org.aero.mtip.XML;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.io.Parser;
import org.aero.mtip.util.ElementData;
import org.aero.mtip.util.FileSelect;
import org.aero.mtip.util.Logger;
import org.aero.mtip.util.TaggedValue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParser extends Parser {
  public XmlParser() {}

  @Override
  public void parse(File file) throws ParserConfigurationException, SAXException, IOException {
    Document doc = FileSelect.createDocument(file);
    doc.getDocumentElement().normalize();

    Node packet = doc.getDocumentElement();
    NodeList dataNodes = packet.getChildNodes();

    // Parse XML and build model based on data
    parseDataNodes(dataNodes);
    addProfileElementData();
  }

  public void parseDataNodes(NodeList dataNodes) {
    int invalidElements = 0;
    int totalElementCount = 0;

    Logger.log(String.format("Parsing %d data nodes.", XmlUtils.asElementListWithTag(dataNodes, XmlTagConstants.DATA).size()));

    for (Node dataNode : XmlUtils.asElementListWithTag(dataNodes, XmlTagConstants.DATA)) { // Loop through all of the <data> nodes
      ElementData modelElement = parseElementData(dataNode);
      totalElementCount++;

      if (!modelElement.isValid()) {
        Logger.log(String.format("Invalid data for element: ", modelElement));
        invalidElements++;
        continue;
      }

      addToModelMap(modelElement);
    }

    checkProperties();
    Logger.log(String.format("Successfully parsed %d/%d data elements.", totalElementCount - invalidElements, totalElementCount));
  }

  public ElementData parseElementData(Node dataNode) {
    ElementData modelElement = new ElementData();

    parseIds(XmlUtils.getFirstChildByTagName(dataNode, XmlTagConstants.ID), modelElement);
    parseType(XmlUtils.getFirstChildByTagName(dataNode, XmlTagConstants.TYPE), modelElement);
    parseAttributes(XmlUtils.getFirstChildByTagName(dataNode, XmlTagConstants.ATTRIBUTES), modelElement);
    parseRelationships(XmlUtils.getFirstChildByTagName(dataNode, XmlTagConstants.RELATIONSHIPS), modelElement);

    return modelElement;
  }

  public void parseIds(Node fieldNode, ElementData modelElement) {
    if (fieldNode == null) {
      Logger.log("Cannot parse IDs. No id element found in data element.");
      return;
    }

    for (Node idNode : XmlUtils.asElementList(fieldNode.getChildNodes())) {
      modelElement.addIdWithType(idNode.getTextContent(), idNode.getNodeName());

      if (idNode.getNodeName().contentEquals(XmlTagConstants.HUDDLE_ID)) {
        modelElement.setImportId(idNode.getTextContent());
      }
    }

    if (modelElement.getImportId().trim().isEmpty() && !modelElement.getIds().isEmpty()) {
      modelElement.setImportId(modelElement.getIds().get(0));
    }
  }

  public void parseType(org.w3c.dom.Element typeElement, ElementData modelElement) {
    if (typeElement == null) {
      Logger.log("Cannot get type from input data. No type element found.");
      return;
    }

    String type = typeElement.getTextContent();

    if (type == null) {
      Logger.log("Cannot get type from input data. No type string found in type element.");
      return;
    }

    if (!type.contains(".")) {
      Logger.log("Cannot get type from input data. Format of type string does not match convention <metamodel.<type>: %s, type");
      return;
    }

    modelElement.setType(type.split("[.]")[1]);
  }

  public void parseAttributes(Node fieldNode, ElementData elementData) {
    if (fieldNode == null) {
      Logger.log("Cannot parse attributes. Attributes fieldNode is null.");
      return;
    }

    for (Node attributeNode : XmlUtils.asElementList(fieldNode.getChildNodes())) {
      if (XmlUtils.isStereotypeAttribute(attributeNode)) {
        parseStereotype(attributeNode, elementData);
        continue;
      }

      if (XmlUtils.isStereotypeTaggedValueAttribute(attributeNode)) {
        parseTaggedValue(attributeNode, elementData);
        continue;
      }

      if (XmlUtils.isDataTypeList(attributeNode)) {
        parseListAttribute(attributeNode, elementData);
        continue;

      }

      parseGenericAttribute(attributeNode, elementData);
    }
  }

  public void parseStereotype(Node attributeNode, ElementData elementData) {
    org.w3c.dom.Element stereotypeNameElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE_NAME);
    org.w3c.dom.Element profileNameElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.ATTRIBUTE_KEY_PROFILE_NAME);

    if (stereotypeNameElement == null) {
      Logger.log(String.format("Cannot parse stereotype. Missing %s element within %s", XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE_NAME,
          XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE));
      return;
    }

    if (profileNameElement == null) {
      Logger.log(String.format("Cannot parse stereotype. Missing %s element within %s", XmlTagConstants.ATTRIBUTE_KEY_PROFILE_NAME,
          XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE));
      return;
    }

    String stereotypeName = stereotypeNameElement.getTextContent();
    String profileName = profileNameElement.getTextContent();

    if (stereotypeName == null || stereotypeName.isEmpty()) {
      Logger.log("Cannot parse stereotype. Stereotype name is null or empty.");
      return;
    }

    if (profileName == null || profileName.isEmpty()) {
      Logger.log("Cannot parse stereotype. Profile name for stereotype is null or empty.");
      return;
    }

    elementData.addStereotype(stereotypeName, profileName);
  }

  public void parseTaggedValue(Node attributeNode, ElementData elementData) {
    org.w3c.dom.Element stereotypeNameElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE_NAME);
    org.w3c.dom.Element profileNameElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.ATTRIBUTE_KEY_PROFILE_NAME);
    org.w3c.dom.Element taggedValueNameElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.TAGGED_VALUE_NAME);
    org.w3c.dom.Element taggedValueValueTypeElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.TAGGED_VALUE_TYPE);
    org.w3c.dom.Element taggedValueValuesElement = XmlUtils.getFirstChildByKey(attributeNode, XmlTagConstants.TAGGED_VALUE_VALUES);
    org.w3c.dom.Element taggedValueValueElement = XmlUtils.getFirstChildByKey(taggedValueValuesElement, XmlTagConstants.TAGGED_VALUE_VALUE);

    if (stereotypeNameElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.ATTRIBUTE_KEY_STEREOTYPE_NAME,
          XmlTagConstants.STEREOTYPE_TAGGED_VALUE));
      return;
    }

    if (profileNameElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.ATTRIBUTE_KEY_PROFILE_NAME,
          XmlTagConstants.STEREOTYPE_TAGGED_VALUE));
      return;
    }

    if (taggedValueNameElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.TAGGED_VALUE_NAME,
          XmlTagConstants.STEREOTYPE_TAGGED_VALUE));
      return;
    }

    if (taggedValueValueTypeElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.TAGGED_VALUE_TYPE,
          XmlTagConstants.STEREOTYPE_TAGGED_VALUE));
      return;
    }

    if (taggedValueValuesElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.TAGGED_VALUE_VALUES,
          XmlTagConstants.STEREOTYPE_TAGGED_VALUE));
      return;
    }

    if (taggedValueValueElement == null) {
      Logger.log(String.format("Cannot create tagged value. Missing %s element in %s.", XmlTagConstants.TAGGED_VALUE_VALUE,
          XmlTagConstants.TAGGED_VALUE_VALUES));
      return;
    }

    TaggedValue tv = new TaggedValue(stereotypeNameElement.getTextContent(), profileNameElement.getTextContent(),
        taggedValueNameElement.getTextContent(), taggedValueValueTypeElement.getTextContent(),
        parseTaggedValueValue(taggedValueValueElement));

    if (!tv.isValid()) {
      return;
    }

    elementData.addStereotypeTaggedValue(tv);
  }

  public String parseTaggedValueValue(org.w3c.dom.Element tvve) {
    // TODO: Add check for list values and return a list of values.
    return tvve.getTextContent();
  }

  public void parseListAttribute(Node attributeNode, ElementData elementData) {
    for (Node listAttribute : XmlUtils.asElementListWithTag(attributeNode.getChildNodes(), XmlTagConstants.ATTRIBUTE)) {
      elementData.addListAttribute(XmlUtils.getKey(listAttribute), listAttribute.getTextContent());
    }
  }

  public void parseGenericAttribute(Node attributeNode, ElementData elementData) {
    // TODO: Ensure generic attribute handling replaces specific constrainedElement handling
    Node childAttributeNode = XmlUtils.getFirstChildByTagName(attributeNode, XmlTagConstants.ATTRIBUTE);

    if (childAttributeNode == null) {
      Logger.log(
          String.format("Cannot parse attribute. No attribute element nested in attribute element for %s", XmlUtils.getKey(attributeNode)));
      return;
    }

    elementData.addAttribute(XmlUtils.getKey(attributeNode), childAttributeNode.getTextContent());
  }

  public String getIdValueFromNode(Node relationship) {
    NodeList idNodes = relationship.getChildNodes();
    for (int k = 0; k < idNodes.getLength(); k++) {
      Node idNode = idNodes.item(k);
      if (idNode.getNodeType() == Node.ELEMENT_NODE) {
        if (idNode.getNodeName() == XmlTagConstants.ID_TAG) {
          return idNode.getTextContent();
        }
      }
    }
    Logger.log("No ID found in field " + relationship.getNodeName() + ".");
    return "";
  }

  public void parseRelationships(Node fieldNode, ElementData elementData) {
    if (fieldNode == null) {
      Logger.log("Cannot get relationships. Relationships fieldNode is null.");
      return;
    }

    for (org.w3c.dom.Element relationshipNode : XmlUtils.asElementList(fieldNode.getChildNodes())) {
      if (relationshipNode.getNodeName().equals(XmlTagConstants.ELEMENT)) {
        parseDiagramElements(relationshipNode, elementData);
        continue;
      }

      if (relationshipNode.getNodeName().equals(XmlTagConstants.DIAGRAM_CONNECTOR)) {
        parseDiagramConnectors(relationshipNode, elementData);
        continue;
      }

      if (relationshipNode.getNodeName().equals(XmlTagConstants.HAS_PARENT)) {
        parseParent(relationshipNode, elementData);
        continue;
      }

      parseRelationshipData(relationshipNode, elementData);
    }
  }

  public void parseDiagramElements(Node elementsNode, ElementData elementData) {
    for (org.w3c.dom.Element elementNode : XmlUtils.asElementList(elementsNode.getChildNodes())) {
      parseDiagramElement(elementNode, elementData);
    }
  }

  public void parseDiagramElement(Node elementNode, ElementData elementData) {
    org.w3c.dom.Element idNode = XmlUtils.getFirstChildByTagName(elementNode, XmlTagConstants.ID);
    org.w3c.dom.Element typeNode = XmlUtils.getFirstChildByTagName(elementNode, XmlTagConstants.TYPE);
    org.w3c.dom.Element relationshipMetadataNode = XmlUtils.getFirstChildByTagName(elementNode, XmlTagConstants.RELATIONSHIP_METADATA);

    if (idNode == null) {
      Logger.log(String.format("Cannot parse diagram element. No %s found in %s for element with id: %s", XmlTagConstants.ID, elementNode.getNodeName(), elementData.getImportId()));
      Logger.log(elementData.toString());
      Logger.log(elementNode.getTextContent());
      return;
    }

    if (typeNode == null) {
      Logger.log(String.format("Cannot parse diagram element. No %s found in %s", XmlTagConstants.TYPE, elementNode.getNodeName()));
      return;
    }

    if (relationshipMetadataNode == null) {
      Logger.log(String.format("Cannot parse diagram element. No %s found in %s", XmlTagConstants.RELATIONSHIP_METADATA,
          elementNode.getNodeName()));
      return;
    }

    String id = idNode.getTextContent();
    String type = typeNode.getTextContent();

    if (id == null || id.isEmpty()) {
      return;
    }

    if (type == null || type.isEmpty()) {
      return;
    }
    // TODO: get properties for diagram formatting HashMap<String, DiagramProperty> properties =
    // getProperties(elementNode);
    elementData.addDiagramElement(id, type, parseBounds(relationshipMetadataNode));
  }

  public Rectangle parseBounds(Node relationshipMetadata) {
    org.w3c.dom.Element topNode = XmlUtils.getFirstChildByTagName(relationshipMetadata, XmlTagConstants.TOP);
    org.w3c.dom.Element bottomNode = XmlUtils.getFirstChildByTagName(relationshipMetadata, XmlTagConstants.BOTTOM);
    org.w3c.dom.Element leftNode = XmlUtils.getFirstChildByTagName(relationshipMetadata, XmlTagConstants.LEFT);
    org.w3c.dom.Element rightNode = XmlUtils.getFirstChildByTagName(relationshipMetadata, XmlTagConstants.RIGHT);

    if (topNode == null || bottomNode == null || leftNode == null || rightNode == null) {
      return new Rectangle(-999, -999, -999, -999);
    }

    try {
      int top = Integer.parseInt(topNode.getTextContent());
      int bottom = Integer.parseInt(bottomNode.getTextContent());
      int left = Integer.parseInt(leftNode.getTextContent());
      int right = Integer.parseInt(rightNode.getTextContent());

      return new Rectangle(left, -top, right - left, -(bottom - top));
    } catch (NumberFormatException nfe) {
      return new Rectangle(-999, -999, -999, -999);
    }
  }

  public void parseDiagramConnectors(Node relationshipNode, ElementData elementData) {
    for (Node elementNode : XmlUtils.asElementList(relationshipNode.getChildNodes())) {
      parseDiagramConnector(elementNode, elementData);
    }
  }

  public void parseDiagramConnector(Node relationshipNode, ElementData elementData) {
    org.w3c.dom.Element idElement = XmlUtils.getFirstChildByTagName(relationshipNode, XmlTagConstants.ID);
    org.w3c.dom.Element typeElement = XmlUtils.getFirstChildByTagName(relationshipNode, XmlTagConstants.TYPE);

    if (idElement == null || typeElement == null) {
      Logger.log(String.format("Cannot parse diagram connector data. No %s or %s tag found in diagramConnector.", XmlTagConstants.ID,
          XmlTagConstants.TYPE));
      return;
    }

    String id = idElement.getTextContent();

    if (id == null) {
      Logger.log(String.format("Cannot parse diagram connector data. Text of %s element is null.", XmlTagConstants.ID));
      return;
    }

    elementData.addDiagramConnector(id);
  }

  public void parseParent(Node relationshipNode, ElementData elementData) {
    elementData.setParent(getIdValueFromNode(relationshipNode));
  }

  public void parseRelationshipData(Node relationshipNode, ElementData elementData) {
    if (XmlUtils.isDataTypeList(relationshipNode)) {
      parseListRelationshipData(relationshipNode, elementData);
      return;
    }

    Node idNode = XmlUtils.getFirstChildByTagName(relationshipNode, XmlTagConstants.ID);

    if (idNode == null) {
      Logger
          .log(String.format("Could not parse relationship node with tag %s. No nested id element found.", relationshipNode.getNodeName()));
      return;
    }

    String id = idNode.getTextContent();

    if (id == null || id.isEmpty()) {
      Logger.log(String.format("Could not parse relationship node with tag %s. Id element has null or empty text.",
          relationshipNode.getNodeName()));
      return;
    }

    elementData.addAttribute(relationshipNode.getNodeName(), id);
  }

  public void parseListRelationshipData(Node relationshipNode, ElementData modelElement) {
    for (Node listItemNode : XmlUtils.asElementList(relationshipNode.getChildNodes())) {
      Node idNode = XmlUtils.getFirstChildByTagName(listItemNode, XmlTagConstants.ID);

      if (idNode == null) {
        Logger.log(
            String.format("Could not parse relationship node with tag %s. No nested id element found.", relationshipNode.getNodeName()));
        return;
      }

      String id = idNode.getTextContent();

      if (id == null || id.isEmpty()) {
        Logger.log(String.format("Could not parse relationship node with tag %s. Id element has null or empty text.",
            relationshipNode.getNodeName()));
        return;
      }

      modelElement.addListAttribute(relationshipNode.getNodeName(), id);
    }
  }

  public void addToModelMap(ElementData modelElement) {
    for (String id : modelElement.getIds()) {
      allElementData.put(id, modelElement);
    }

    if (modelElement.isStereotype()) {
      for (String id : modelElement.getIds()) {
        stereotypeElementData.put(id, modelElement);
      }
    }

    if (modelElement.isProperty() && modelElement.getParentImportId() == null) { // Property parent may not exists yet. Check later if property of
      // stereotype.
      properties.add(modelElement);
    }

    if (allElementData.containsKey(modelElement.getParentImportId()) && modelElement.isProperty()
        && allElementData.get(modelElement.getParentImportId()).isStereotype()) {
      stereotypeElementData.put(modelElement.getImportId(), modelElement);
    }
  }
  
  // TODO: Re-factor these into Importer
  /**
   * Checks all cached element data in properties to determine if they are properties of stereotypes
   * and required by stereotypeElementData. Stereotypes and associated properties must be created
   * before model elements depending on them.
   */
  public void checkProperties() {
    for (ElementData modelElement : properties) {
      if (modelElement.getParentImportId() == null) {
        continue;
      }

      if (allElementData.get(modelElement.getParentImportId()) == null) {
        continue;
      }

      if (allElementData.get(modelElement.getParentImportId()).isStereotype()) {
        stereotypeElementData.put(modelElement.getImportId(), modelElement);
        continue;
      }

      Logger.log(String.format("Cannot determine if property is property of stereotype: %s", modelElement));
    }
  }

  public void addProfileElementData() {
    for (ElementData elementData : stereotypeElementData.values()) {
      profileElementData.put(elementData.getImportId(), elementData);

      if (elementData.getParentImportId() == null || elementData.getParentImportId().isEmpty()) {
        continue;
      }

      addParentToProfileData(elementData.getParentImportId());
    }
  }

  /**
   * Recursively adds parent elements to the profile data
   * 
   * @param parentID Id of element to recursively add to profile data
   */
  public void addParentToProfileData(String parentID) {
    profileElementData.put(parentID, allElementData.get(parentID));

    ElementData parentData = allElementData.get(parentID);

    if (parentData == null || parentData.getParentImportId() == null || parentData.getParentImportId().isEmpty()) {
      return;
    }

    addParentToProfileData(parentData.getParentImportId());
  }
}
