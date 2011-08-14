package org.chon.cms.wiki.nodes;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.wiki.app.WikiApplication;

public class WikiChonModel {
	
	public IContentNode install(ContentModel cm) {
		IContentNode wwwNode = cm.getPublicNode();
		try {
			Node node = wwwNode.getNode().addNode(WikiApplication.WIKI_PAGE_NOT_EXISTS_REDIRECT);
			String type = WikiApplication.WIKI_NEW_PAGE_NODE_TYPE;
			node.setProperty("type", type);
			registerWikiCreatorPageNodeType(cm);
			registerWikiNodeType(cm);
			cm.getSession().save();
			return cm.getContentNode(node);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void registerWikiCreatorPageNodeType(ContentModel cm) throws RepositoryException {
		String type = WikiApplication.WIKI_NEW_PAGE_NODE_TYPE;
		
		Map<String, String> defValues = new HashMap<String, String>();
		defValues.put("template", "pages/wiki_new_page.html");
		
		registerType(
				cm,
				type,
				WikiNewPageNodeRenderer.class.getName(),
				WikiPageContentNode.class.getName(),
				null, defValues);
	}
	
	private void registerWikiNodeType(ContentModel cm) throws RepositoryException {
		String type = WikiApplication.WIKI_PAGE_NODE_TYPE;
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("bubbleToParnet", "true");
		
		Map<String, String> defValues = new HashMap<String, String>();
		defValues.put("template", "pages/wiki.html");
		
		registerType(
				cm,
				type,
				WikiPageNodeRenderer.class.getName(),
				WikiPageContentNode.class.getName(),
				properties, defValues);
	}
	
	
	private void registerType(ContentModel cm, String typeName, String renderer, String contentNode, Map<String, String> properties, Map<String, String> defProperties) throws RepositoryException {
		if(properties == null) {
			properties = new HashMap<String, String>();
		}
		properties.put("renderer", renderer);
		properties.put("contentNode", contentNode);
		registerType(cm, typeName, properties, defProperties);
	}
	
	private void registerType(ContentModel cm, String typeName, Map<String, String> properties, Map<String, String> defProperties) throws RepositoryException {
		Node etcTypes = cm.getConfigNode().getChild("types").getNode();
		if(etcTypes.hasNode(typeName)) {
			throw new ItemExistsException();
		}
		
		Node typeNode = etcTypes.addNode(typeName);
		addProperties(typeNode, properties);
		typeNode.setProperty("type", "typeDesc");
		
		Node defProps = typeNode.addNode("default.properties");
		addProperties(defProps, defProperties);
		defProps.setProperty("type", "config");
	}

	private void addProperties(Node node, Map<String, String> properties) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
		if(properties != null) {
			for(String k : properties.keySet()) {
				node.setProperty(k, properties.get(k));
			}
		}
	}
}
