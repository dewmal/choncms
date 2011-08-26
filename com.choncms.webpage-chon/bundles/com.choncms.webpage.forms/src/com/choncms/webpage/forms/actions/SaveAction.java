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
import org.json.XML;

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
				createOrEdit(formName, formData);
				params.put("formName", formName);
				params.put("formData", XML.escape(formData));
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resp.formatTemplate(prefix + "/editForm.html", params);
	}

	private void createOrEdit(String formName, String formData)
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
		formNode.getSession().save();
	}
}
