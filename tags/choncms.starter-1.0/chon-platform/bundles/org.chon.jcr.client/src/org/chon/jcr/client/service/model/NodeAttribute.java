package org.chon.jcr.client.service.model;

public class NodeAttribute {
	
	public String name;
	
	public AttributeType type;
	
	public String data;

	public NodeAttribute(String name, AttributeType type, String data) {
		this.name = name;
		this.type = type;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
