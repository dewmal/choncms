package com.choncms.webpage.forms.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.json.XML;

import com.choncms.webpage.forms.WorkflowUtils;
import com.choncms.webpage.forms.workflow.Workflow;

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
				boolean isFileUploadEnabled = req.get("isFileUploadEnabled") != null;
				
				createOrEdit(formName, formData, successData, errorData, workflow, workflowConfig, isFileUploadEnabled);
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
				params.put("formName", formName);
				params.put("formData", XML.escape(formData));
				params.put("successData", XML.escape(successData));
				params.put("errorData", XML.escape(errorData));
				params.put("workflow", workflow);
				params.put("workflowConfig", workflowConfig);
				params.put("isFileUploadEnabled", isFileUploadEnabled);
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
