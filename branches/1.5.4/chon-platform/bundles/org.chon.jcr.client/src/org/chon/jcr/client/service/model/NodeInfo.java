package org.chon.jcr.client.service.model;

import java.util.List;

import javax.jcr.Node;

public class NodeInfo {
	private String id;
	private String name;
	private String path;
	private String type;
	
	private NodeAttribute[] attributes;
	private List<NodeInfo> childs;
	private Node node;
	
	public NodeInfo(String id, String name, String path, NodeAttribute[] attributes,
			List<NodeInfo> childs, String type, Node node) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.attributes = attributes;
		this.childs = childs;
		this.type = type;
		this.node = node;
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getPath() {
		return path;
	}

	public NodeAttribute[] getAttributes() {
		return attributes;
	}
	public List<NodeInfo> getChilds() {
		return childs;
	}

	public String getType() {
		return type;
	}
	
	public Node getNode() {
		return node;
	}
}
