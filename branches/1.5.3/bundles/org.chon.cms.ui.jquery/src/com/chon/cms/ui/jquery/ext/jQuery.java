package com.chon.cms.ui.jquery.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.model.renderers.VTplNodeRenderer;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class jQuery {
	private static final Log log = LogFactory.getLog(jQuery.class);
	
	private IContentNode node;
	private Response resp;
	private JCRApplication app;
	private Request req;
	private JQueryConfig config;
	public jQuery(Request req, Response resp, IContentNode node,
			JCRApplication app, JQueryConfig config) {
		this.node = node;
		this.resp = resp;
		this.app = app;
		this.req = req;
		this.config = config;
		init(resp);
	}
	private void init(Response resp) {
		if(config.isAutoInclude()) {
			@SuppressWarnings("unchecked")
			List<String> scrips = (List<String>) resp.getTemplateContext().get("head:scripts");
			@SuppressWarnings("unchecked")
			List<String> css = (List<String>) resp.getTemplateContext().get("head:css");
			
			css.add("jquery/css/smoothness/jquery-ui-1.8.9.custom.css");
			
			scrips.add("jquery/js/jquery-1.4.4.min.js");
			scrips.add("jquery/js/jquery-ui-1.8.9.custom.min.js");
		}
	}
	
	/**
	 * Render accordion from child nodes (paths) on a node (parent)
	 * @param parent
	 * @param paths
	 * @return
	 */
	public String accordion(String parent, List<String> paths) {
		try {
			Node root = node.getNode().getParent().getNode(parent);
			List<IContentNode> nodes = new ArrayList<IContentNode>();
			for(String s : paths) {
				ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
				nodes.add(cm.getContentNode(root.getNode(s)));
			}
			return accordion(nodes);
		} catch (PathNotFoundException e) {
			return "Can't find node: " + e.getMessage();
		} catch (RepositoryException e) {
			log.error("Repo exception", e);
		}
		//should not get here
		return null;
	}
	
	/**
	 * render accordion on list of nodes
	 * 
	 * @param nodes
	 * @return
	 */
	public String accordion(List<IContentNode> nodes) {
		Map<String, Object> params = new HashMap<String, Object>();
		VTplNodeRenderer.prepareParams(node, req, resp, params, app);
		params.put("nodes", nodes);
		params.put("uuid", (int)(Math.random()*1000));
		return resp.formatTemplate("jquery/accordion/accordion.html", params);
	}
	
	public String accordion(IContentNode node, String order) throws RepositoryException {
		ContentModel cm = node.getContentModel();
		QueryResult qr = cm.query(
				"SELECT * from [nt:unstructured] as x WHERE ischildnode(x, '"
						+ node.getAbsPath() + "') " + order, Query.JCR_SQL2,
				null, null);
		
		List<IContentNode> nodes = new ArrayList<IContentNode>();
		NodeIterator it = qr.getNodes();
		while(it.hasNext()) {
			IContentNode n = cm.getContentNode(it.nextNode());
			nodes.add(n);
		}
		return accordion(nodes);
	}
}
