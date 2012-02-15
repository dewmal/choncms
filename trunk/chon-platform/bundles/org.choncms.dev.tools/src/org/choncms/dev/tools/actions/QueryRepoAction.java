package org.choncms.dev.tools.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.chon.cms.model.ContentModel;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONObject;

public class QueryRepoAction implements Action {
	
	private String actionPrefix;
	private JSONObject config;

	public QueryRepoAction(String actionPrefix, JSONObject config) {
		this.actionPrefix = actionPrefix;
		this.config = config;
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		Map<String, Object> params = new HashMap<String, Object>();

		try {
			String query = req.get("query");
			if (query != null) {
				params.put("query", query);
				params.put("utils", new Utils());
				int maxResults = config.optInt("maxResults", 5);
				params.put("maxResults", maxResults);
				QueryResult result = cm.query(query, Query.JCR_SQL2, 0, maxResults+1);
				NodeIterator ni = result.getNodes();
				List<Node> list = new ArrayList<Node>();
				while (ni.hasNext()) {
					list.add(ni.nextNode());
				}
				params.put("list", list);
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			params.put("error", e.getMessage());
		}
		return resp.formatTemplate(actionPrefix + "/queryRepo.html", params);
	}
}