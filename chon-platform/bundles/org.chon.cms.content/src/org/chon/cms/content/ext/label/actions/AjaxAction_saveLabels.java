package org.chon.cms.content.ext.label.actions;

import javax.jcr.Node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.content.ext.label.LabelsExtension;
import org.chon.cms.model.ContentModel;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONArray;
import org.json.JSONObject;


public class AjaxAction_saveLabels implements Action {
	private static final Log log = LogFactory.getLog(AjaxAction_saveLabels.class);

	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		Node labelsNode = cm.getAppsConfigNode(LabelsExtension.NODE_NAME).getNode();
		try {
			JSONObject json = new JSONObject(req.get("data"));
			JSONArray arr_del = json.getJSONArray("deletedFields");
			for (int i = 0; i < arr_del.length(); i++) {
				String n = arr_del.getString(i);
				labelsNode.getProperty(n).remove();
			}

			JSONArray arr_fields = json.getJSONArray("fields");
			for (int i = 0; i < arr_fields.length(); i++) {
				JSONObject p = arr_fields.getJSONObject(i);
				String name = p.getString("name");
				String value = p.getString("value");
				if (p.getBoolean("isNew")) {
					name = "label." + name;
				}
				labelsNode.setProperty(name, value);
			}
			labelsNode.getSession().save();
		} catch (Exception e) {
			log.error("Error while saving labels", e);
			return e.getMessage();
		}
		return "OK";
	}
}