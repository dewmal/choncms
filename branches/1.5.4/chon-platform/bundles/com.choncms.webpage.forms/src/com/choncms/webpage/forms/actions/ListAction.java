package com.choncms.webpage.forms.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class ListAction extends AbstractFormsAction {

	public ListAction(String prefix, IContentNode appFormDataNode) {
		super(prefix, appFormDataNode);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<IContentNode> list = appFormDataNode.getChilds();
		params.put("list", list);
		return resp.formatTemplate(prefix + "/list.html", params);
	}
}