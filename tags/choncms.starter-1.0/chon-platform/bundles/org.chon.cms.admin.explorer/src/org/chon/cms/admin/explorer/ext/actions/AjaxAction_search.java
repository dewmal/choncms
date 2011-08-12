package org.chon.cms.admin.explorer.ext.actions;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.model.ContentModel;
import org.chon.jcr.client.service.model.NodeInfo;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONArray;
import org.json.JSONException;

public class AjaxAction_search implements Action {
	private static final Log log = LogFactory.getLog(AjaxAction_search.class);
	private static final Integer LIMIT = 100;

	private AjaxAction_grid gridAjaxAction;

	public AjaxAction_search(AjaxAction_grid gridAjaxAction) {
		this.gridAjaxAction = gridAjaxAction;
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);		
		String q = req.get("q");
		try {
			String query = "select * from [nt:unstructured] as r WHERE name(r)='"+q+"' OR lower(r.[name]) LIKE '%"+q.toLowerCase()+"%' OR contains(r.*,'"+q+"')";
			QueryResult qr = cm.query(query, Query.JCR_SQL2, 0, LIMIT);
			List<NodeInfo> list = new ArrayList<NodeInfo>();
			NodeIterator it = qr.getNodes();
			while(it.hasNext()) {
				Node node = it.nextNode();
				NodeInfo ni = new NodeInfo(node.getIdentifier(), node.getName(), node.getPath(), null, null, cm.getNodeType(node), node);
				list.add(ni);
			}
			JSONArray arr = gridAjaxAction.niNodesToGrid(req, list);
			return arr.toString();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
