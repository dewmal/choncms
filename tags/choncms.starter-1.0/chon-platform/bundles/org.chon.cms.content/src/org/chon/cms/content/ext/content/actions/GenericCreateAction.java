package org.chon.cms.content.ext.content.actions;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.model.ContentModel;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public class GenericCreateAction implements Action {

	private String pageTitle;
	private String pageNote;
	private String type;
	private String suffix;
	private String pid;
	private String nextAction;
	
	private Map<String, String> properties = new HashMap<String, String>();
	
	public GenericCreateAction(String pageTitle, String pageNote, String type,
			String suffix, String pid, String nextAction) {
		this.pageTitle = pageTitle;
		this.pageNote = pageNote;
		this.type = type;
		this.suffix = suffix;
		this.pid = pid;
		this.nextAction = nextAction;
	}

	public Map<String, String> getProperties() {
		return properties;
	}


	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageTitle", pageTitle);
		params.put("pageNote", pageNote);
		params.put("type", type);
		params.put("suffix", suffix);
		params.put("properties", properties);
		if(pid == null) {
			pid = cm.getUserHome().getId();
		}
		if(nextAction == null) {
			nextAction = "content.edit.do";
		}
		params.put("pid", pid);
		params.put("nextAction", nextAction);
		return resp.formatTemplate("admin/content/generic.create.html", params);
	}
}
