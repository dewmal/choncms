package com.choncms.webpage.forms.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.json.XML;

import com.choncms.webpage.forms.WorkflowUtils;
import com.choncms.webpage.forms.workflow.Workflow;

public class EditAction extends AbstractFormsAction {

	public EditAction(String prefix, IContentNode appFormDataNode) {
		super(prefix, appFormDataNode);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		String formName = req.get("name");
		IContentNode formNode = appFormDataNode.getChild(formName);
		Map<String, Object> params = new HashMap<String, Object>();
		if (formName != null) {
			String formData = formNode.get("data");
			params.put("formName", formName);
			params.put("formData", XML.escape(formData));
			
			String workflow = formNode.get("workflow");
			if(workflow != null) {
				params.put("workflow", workflow);
			} else {
				params.put("workflow", WorkflowUtils.getWorkflow(null).getName());
			}
			List<String> availableWorkfows = new ArrayList<String>();
			try {
				Workflow[] wfs = WorkflowUtils.getRegisteredWorkflows();
				if(wfs != null) {
					for(Workflow w : wfs) {						
						availableWorkfows.add(w.getName());
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			params.put("availableWorkfows", availableWorkfows.toString());
			
			
			String workflowConfig = formNode.get("workflowConfig");
			if(workflowConfig != null) {
				params.put("workflowConfig", XML.escape(workflowConfig));
			}
			
			String successData = formNode.get("successData");
			if(successData != null) {
				params.put("successData", XML.escape(successData));
			}
			String errorData = formNode.get("errorData");
			if(errorData != null) {
				params.put("errorData", XML.escape(errorData));
			}
		}
		return resp.formatTemplate(prefix + "/editForm.html", params);
	}
}