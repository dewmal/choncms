package com.choncms.webpage.forms;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

import com.choncms.webpage.forms.actions.EditAction;
import com.choncms.webpage.forms.actions.ListAction;
import com.choncms.webpage.forms.actions.SaveAction;

public class FormsExtension implements Extension {
	
	
	private Map<String, Action> actions = new HashMap<String, Action>();
	private IContentNode appFormDataNode;
	private String prefix;
	
	public FormsExtension(JCRApplication app, String prefix, IContentNode appFormDataNode) {
		this.prefix = prefix;
		this.appFormDataNode = appFormDataNode;
		actions.put(prefix + ".list", new ListAction(prefix, appFormDataNode));
		actions.put(prefix + ".edit", new EditAction(prefix, appFormDataNode));
		actions.put(prefix + ".save", new SaveAction(prefix, appFormDataNode));
	}

	@Override
	public Map<String, Action> getAdminActons() {
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		return new FormsFE(prefix, req, resp, appFormDataNode, this);
	}

	
	public void processFormSubmition(IContentNode formNode, Request req) {
		@SuppressWarnings("unchecked")
		Map<String, String> params = req.getServletRequset().getParameterMap();		
		System.out.println("FormsExtension.processFormSubmition()");
		System.out.println(params);
		//formNode.getWorkflow().run(submittedData) ... 
		System.out.println("FormsExtension.processFormSubmition()");
	}

}
