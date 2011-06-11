package org.chon.cms.content.ext.label;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.content.ext.label.actions.Action_labels;
import org.chon.cms.content.ext.label.actions.AjaxAction_saveLabels;
import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;


public class LabelsExtension implements Extension {

	//private static final Log log = LogFactory.getLog(LabelsExtension.class);

	public static final String NODE_NAME = "labels";

	private Map<String, Action> actions = new HashMap<String, Action>();
	private Map<String, Action> ajaxActions = new HashMap<String, Action>();

	public LabelsExtension(JCRApplication app) {
		actions.put("labels", new Action_labels());
		ajaxActions.put("saveLabels", new AjaxAction_saveLabels());
	}

	@Override
	public Map<String, Action> getAdminActons() {
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		return ajaxActions;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		IContentNode labelsNode = cm.getAppsConfigNode(NODE_NAME, true);
		LabelsView lv = new LabelsView(labelsNode);
		return lv;
	}
}
