package org.chon.cms.admin.explorer.ext.actions;

import java.util.Date;
import java.util.List;

import javax.jcr.PathNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.admin.explorer.ext.ExplorerExtension;
import org.chon.cms.core.Repo;
import org.chon.cms.model.ContentModel;
import org.chon.jcr.client.service.RepoService;
import org.chon.jcr.client.service.model.NodeInfo;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AjaxAction_grid implements Action {
	private static final Log log = LogFactory.getLog(AjaxAction_grid.class);
	
	private ExplorerExtension explorerExtension = null;

	public AjaxAction_grid(ExplorerExtension explorerExtension) {
		this.explorerExtension = explorerExtension;
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		RepoService repoSvc = Repo.getRepoService();
		
		String id = req.get("id");
		try {
			NodeInfo node = repoSvc.getNode(cm.getSession(), id, 1, false);
			JSONArray arr = niNodesToGrid(req, node.getChilds());
			return arr.toString();
		} catch (Exception e) {
			log.error("Error while getting grid data", e);
		}
		return null;
	}

	public JSONArray niNodesToGrid(Request req, List<NodeInfo> nodes)
			throws JSONException {
		JSONArray arr = new JSONArray();
		for (NodeInfo ni : nodes) {
			if (explorerExtension.notFiltered(req, ni)) {
				JSONObject j = new JSONObject();
				j.put("id", ni.getId());
				j.put("name", ni.getName());

				putDateModified(ni, j);

				j.put("type", ni.getType());

				j.put("cls", "type-" + ni.getType().replaceAll("\\:|\\.", "-"));
				j.put("path", ni.getPath());
				// TODO
				// j.put("details", "details");

				arr.put(j);
			}
		}
		return arr;
	}

	private void putDateModified(NodeInfo ni, JSONObject j) {
		try {
			Date lastModified = ni.getNode().getProperty("jcr:lastModified").getDate().getTime();
			j.put("dateModified", ExplorerExtension.dateToJSONString(lastModified));
		} catch (PathNotFoundException e) {
			log.warn("Node " + ni.getName() + " does not conain jcr:lastModified");
		} catch (Exception e) {
			log.warn("Error getting date for node " + ni.getName(), e);
		}
	}
}