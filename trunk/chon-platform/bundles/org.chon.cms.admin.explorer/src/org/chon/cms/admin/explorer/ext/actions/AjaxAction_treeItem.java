package org.chon.cms.admin.explorer.ext.actions;

import java.util.List;

import javax.jcr.RepositoryException;

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
import org.json.JSONObject;

public class AjaxAction_treeItem implements Action {
	private ExplorerExtension explorerExtension;
	public AjaxAction_treeItem(ExplorerExtension explorerExtension) {
		this.explorerExtension = explorerExtension;
	}
	
	@Override
	public String run(Application app, Request req, Response resp) {
		
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		String username = cm.getUser().getName();
		
		RepoService repoSvc = Repo.getRepoService();
		String id = req.get("node");
		if ("root".equals(id)) {
			try {
				id = cm.getSession().getRootNode().getIdentifier(); // facade.getNode(ContentNode.www_node_name).getIdentifier();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
		try {
			NodeInfo node = repoSvc.getNode(cm.getSession(), id, 2, false);
			JSONArray arr = new JSONArray();
			List<NodeInfo> childs = node.getChilds();
			for (NodeInfo ni : childs) {
				if (explorerExtension.notFiltered(req, ni)) {
					boolean leaf = ni.getChilds() == null
							|| "nt:file".equals(ni.getType());
					JSONObject j = new JSONObject();
					j.put("text", ni.getName());
					j.put("leaf", leaf);
					j.put("id", ni.getId());
					j.put("cls", "type-" + ni.getType().replaceAll("\\:|\\.", "-"));
					j.put("path", ni.getPath());
					j.put("type", ni.getType());
					if ("sys.public".equals(ni.getType()) || "sys.home".equals(ni.getType())) {
						j.put("expanded", true);
					}
					if(username.equals(ni.getName())) {
						j.put("expanded", true);
					}
					arr.put(j);
				}
			}
			return arr.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
