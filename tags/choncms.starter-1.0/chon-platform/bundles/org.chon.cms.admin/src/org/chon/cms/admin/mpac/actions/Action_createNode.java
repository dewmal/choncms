package org.chon.cms.admin.mpac.actions;

import javax.jcr.Node;

import org.chon.cms.admin.utils.JSONUtils;
import org.chon.cms.admin.utils.NodeView;
import org.chon.cms.model.ContentModel;
import org.chon.core.common.util.CyrToLat;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONObject;

public class Action_createNode implements Action {

	@Override
	public String run(Application app, Request req, Response resp) {
		try {
			JSONObject nc = new JSONObject( req.get("node"));
			String name = CyrToLat.cyrlicToLat(nc.getString("name").trim(), false, ".-_", '-');
			nc.put("name", name);
			ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
			Node n = JSONUtils.createNode(cm.getSession(), nc);
			JSONObject rv = JSONUtils.msg("OK");
			return rv.put("node", NodeView.toJSONObject(n)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return JSONUtils.errorMsg(e, "Invalid JSON node creation structure").toString();
		}
	}

}