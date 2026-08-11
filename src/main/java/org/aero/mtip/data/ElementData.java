/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */
package org.aero.mtip.data;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.annotation.CheckForNull;
import org.aero.mtip.constants.DoDAFConstants;
import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.util.TaggedValue;

public class ElementData {
	private String type = "";
	private String category = "";

	private String importId = "";
	private String parent = "";
	private String client = "";
	private String supplier = "";
	private String name = "";
	private Rectangle diagramFrame = null;

	private HashMap<String, String> ids = new HashMap<String, String>();
	private HashMap<String, String> stereotypes = new HashMap<String, String>();
	private HashMap<String, String> attributes = new HashMap<String, String>();
	private HashMap<String, List<String>> listAttributes = new HashMap<String, List<String>>();

	private List<String> diagramConnectors = new ArrayList<String>();

	private HashMap<String, Rectangle> diagramElementLocations = new HashMap<String, Rectangle>();

	private List<TaggedValue> taggedValues = new ArrayList<TaggedValue>();

	public ElementData() {

	}

	public void setType(String type) {
		this.type = type;
		setCategory();
	}

	public void addIdWithType(String id, String idSource) {
		this.ids.put(id, idSource);
	}

	public List<String> getIds() {
		return new ArrayList<String>(ids.keySet());
	}

	public void setImportId(String importId) {
		this.importId = importId;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void addAttribute(String key, String value) {
		if (key.contentEquals(XmlTagConstants.SUPPLIER)) {
			setSupplier(value);
			return;
		}

		if (key.contentEquals(XmlTagConstants.CLIENT)) {
			setClient(value);
			return;
		}

		if (key.equals(XmlTagConstants.NAME)) {
			// Re-factor with collection of forbidden characters
			value = value.replace("(", "").replace(")", "").replace("#", "No.").replace("\"", "");
			setName(value);
			return;
		}

		attributes.put(key, value);
	}

	public void addListAttribute(String key, String value) {
		if (!listAttributes.containsKey(key)) {
			listAttributes.put(key, new ArrayList<String>());
		}

		listAttributes.get(key).add(value);
	}

	public List<String> getListAttributes(String key) {
		if (hasListAttributes(key)) {
			return listAttributes.get(key);
		}

		return new ArrayList<String>();
	}

	public boolean hasListAttributes(String key) {
		if (listAttributes.containsKey(key)) {
			return true;
		}
		return false;
	}

	public Rectangle getLocation(String key) {
		if (!diagramElementLocations.containsKey(key)) {
			return new Rectangle(-999, -999, -999, -999);
		}

		return diagramElementLocations.get(key);
	}

	public String printAttributes() {
		String list = "";
		for (String key : attributes.keySet()) {
			list += key + ", ";
		}
		list = list.substring(0, list.length() - 2);
		return list;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public void addDiagramConnector(String relationship) {
		this.diagramConnectors.add(relationship);
	}

	public void addStereotype(String stereotypeName, String profileName) {
		stereotypes.put(stereotypeName, profileName);
	}

	public HashMap<String, String> getStereotypes() {
		return stereotypes;
	}

	public boolean hasStereotypes() {
		return !stereotypes.isEmpty();
	}

	public boolean hasAttribute(String key) {
		Set<String> keys = attributes.keySet();
		if (keys.contains(key)) {
			return true;
		}
		return false;
	}

	public List<String> getDiagramConnectorIds() {
		return diagramConnectors;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getCategory() {
		return category;
	}

	public String getImportId() {
		return importId;
	}

	public String getParentImportId() {
		return parent;
	}

	public String getAttribute(String key) {
		return attributes.get(key);
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public String getClientImportId() {
		return client;
	}

	public String getSupplierImportId() {
		return supplier;
	}

	public String getName() {
		return name;
	}

	private void setCategory() {
		if (SysmlConstants.SYSML_ELEMENTS.contains(type) || UAFConstants.UAF_ELEMENTS.contains(type)) {
			category = SysmlConstants.ELEMENT;
		}
		if (SysmlConstants.SYSML_RELATIONSHIPS.contains(type) || UAFConstants.UAF_RELATIONSHIPS.contains(type)) {
			category = SysmlConstants.RELATIONSHIP;
		}
		if (SysmlConstants.SYSML_DIAGRAMS.contains(type) || UAFConstants.UAF_DIAGRAMS.contains(type)
				|| DoDAFConstants.DODAF_DIAGRAMS.contains(type)) {
			category = SysmlConstants.DIAGRAM;
		}
	}

	public String toString() {
		return String.format("\n\tParent: %s\n\tType: %s\n\tName: %s\n\tID: %s\n\tOwner: %s)", getParentImportId(),
				getType(), getName(), getImportId(), getParentImportId());
	}

	public void addStereotypeTaggedValue(TaggedValue tv) {
		taggedValues.add(tv);
	}

	public List<TaggedValue> getTaggedValues() {
		return taggedValues;
	}

	public boolean hasTaggedValues() {
		if (taggedValues.isEmpty()) {
			return false;
		}
		return true;
	}

	@CheckForNull
	public Rectangle getDiagramFrame() {
		return diagramFrame;
	}

	public void setDiagramFrame(Rectangle diagramFrame) {
		this.diagramFrame = diagramFrame;
	}

	public boolean isProperty() {
		return getType().contentEquals(SysmlConstants.PROPERTY);
	}

	public boolean isStereotype() {
		return getType().contentEquals(SysmlConstants.STEREOTYPE);
	}

	public boolean isValid() {
		return getImportId() != null && !getImportId().isEmpty();
	}
}
