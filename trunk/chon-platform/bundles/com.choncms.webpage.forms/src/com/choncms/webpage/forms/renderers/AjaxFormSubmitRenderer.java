package com.choncms.webpage.forms.renderers;

import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.INodeRenderer;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;

import com.choncms.webpage.forms.ext.FormsFE;

public class AjaxFormSubmitRenderer implements INodeRenderer {

	@Override
	public void render(IContentNode node, Request req, Response resp,
			Application app, ServerInfo serverInfo) {
		
		if("js".equals(req.get("init"))) {
			resp.getOut().println("if(!this.chon) chon={};");
			resp.getOut().println("chon.forms.AJAX_POST_NODE='"+node.getPath()+"';");
			return;
		}
		
		IContentNode formDataNode = node.getContentModel().getContentNode(node.get("formDataNodeAbsPath"));
		String sf_val = req.get("__submit_form");
		String formName = req.get("__formName");
		
		if("true".equals(sf_val)) {
			IContentNode formNode = formDataNode.getChild(formName);
			String out = FormsFE.getFormSubmissionResponse(app, req, formNode);
			resp.getOut().print(out);
			return;
		}
		
		resp.getOut().print("Invalid form submission");
	}

}
