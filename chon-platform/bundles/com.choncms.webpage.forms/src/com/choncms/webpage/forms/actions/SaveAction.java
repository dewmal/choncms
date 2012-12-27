package com.choncms.webpage.forms.actions;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.json.JSONObject;
import org.json.XML;

import com.choncms.webpage.forms.WorkflowUtils;

public class SaveAction extends AbstractFormsAction {

	public SaveAction(String prefix, IContentNode appFormDataNode) {
		super(prefix, appFormDataNode);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		Map<String, Object> params = new HashMap<String, Object>();
		String formName = req.get("formName");
		if (formName != null) {
			try {
				String formData = req.get("formData");
				String successData = req.get("successData","Thank you!");
				String errorData = req.get("errorData","Oooops, an error occured.");
				String workflow = req.get("workflow");
				String workflowConfig = req.get("workflowConfig");
				if (workflowConfig.trim().length() == 0) {
					workflowConfig = "{}";
				}
				workflowConfig = new JSONObject(workflowConfig).toString(2);
				boolean isFileUploadEnabled = req.get("isFileUploadEnabled") != null;
				
				createOrEdit(formName, formData, successData, errorData, workflow, workflowConfig, isFileUploadEnabled);
				
				params.put("availableWorkfows",  WorkflowUtils.getRegisteredWorkflows());
				params.put("formName", formName);
				params.put("formData", XML.escape(formData));
				params.put("successData", XML.escape(successData));
				params.put("errorData", XML.escape(errorData));
				params.put("workflow", workflow);
				params.put("workflowConfig", workflowConfig);
				params.put("isFileUploadEnabled", isFileUploadEnabled);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				params.put("error", e.getMessage());
			}
		}
		return resp.formatTemplate(prefix + "/editForm.html", params);
	}

	private void createOrEdit(String formName, String formData, String successData, String errorData, String workflow, String workflowConfig, boolean isFileUploadEnabled)
			throws PathNotFoundException, ItemExistsException,
			VersionException, ConstraintViolationException, LockException,
			RepositoryException {
		Node node = appFormDataNode.getNode();
		Node formNode = null;
		if (node.hasNode(formName)) {
			formNode = node.getNode(formName);
		} else {
			formNode = node.addNode(formName);
			formNode.setProperty("type", "form");
		}
		formNode.setProperty("data", formData);
		formNode.setProperty("successData", successData);
		formNode.setProperty("errorData", errorData);
		formNode.setProperty("workflow", workflow);
		formNode.setProperty("workflowConfig", workflowConfig);
		if(isFileUploadEnabled) {
			formNode.setProperty("isFileUploadEnabled", true);
		} else {
			formNode.setProperty("isFileUploadEnabled", false);
		}
		formNode.getSession().save();
	}
}
