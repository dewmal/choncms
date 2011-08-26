package com.choncms.webpage.forms;

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

import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.XML;

public class FormsExtension implements Extension {
	private Map<String, Action> actions = new HashMap<String, Action>();

	public FormsExtension(JCRApplication app, final String prefix, final IContentNode appFormDataNode) {
		actions.put(prefix + ".list", new Action() {
			
			@Override
			public String run(Application app, Request req, Response resp) {
				Map<String, Object> params = new HashMap<String, Object>();
				List<IContentNode> list = appFormDataNode.getChilds();
				params.put("list", list);
				return resp.formatTemplate(prefix + "/list.html", params);
			}
		});
		
		actions.put(prefix + ".edit", new Action() {

			@Override
			public String run(Application app, Request req, Response resp) {
				String formName = req.get("name");
				IContentNode formNode = appFormDataNode.getChild(formName);
				String formData = formNode.get("data");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("formName", formName);
				params.put("formData", XML.escape(formData));
				return resp.formatTemplate(prefix + "/editForm.html", params);
			}
			
		});
		//this.app = app;
		actions.put(prefix + ".create", new Action() {
			@Override
			public String run(Application app, Request req, Response resp) {
				Map<String, Object> params = new HashMap<String, Object>();
				String formName = req.get("formName");
				if(formName != null) {
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

			private void createOrEdit(String formName, String formData) throws PathNotFoundException, ItemExistsException, VersionException, ConstraintViolationException, LockException, RepositoryException {
				Node node = appFormDataNode.getNode();
				Node formNode = null;
				if(node.hasNode(formName)) {
					formNode = node.getNode(formName);
				} else {
					formNode = node.addNode(formName);
					formNode.setProperty("type", "form");
				}
				formNode.setProperty("data", formData);
				formNode.getSession().save();
			}
		});
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
		// TODO Auto-generated method stub
		return null;
	}

}
