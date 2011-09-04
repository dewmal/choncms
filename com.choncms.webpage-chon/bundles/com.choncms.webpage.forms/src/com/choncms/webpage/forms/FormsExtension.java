package com.choncms.webpage.forms;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
	private JCRApplication app;
	
	public FormsExtension(JCRApplication app, String prefix, IContentNode appFormDataNode) {
		this.app = app;
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
		return new FormsFE(app, prefix, req, resp, appFormDataNode, this);
	}

	
	@SuppressWarnings("unchecked")
	public Map<String, Object> processFormSubmition(IContentNode formNode, Request req) {
		Map<String, Object> params = req.getServletRequset().getParameterMap();
		Map<String, Object> formData = new HashMap<String, Object>();
		formData.put("ctx", new HashMap<String, Object>());
		
		System.out.println("FormsExtension.processFormSubmition()");
		for(String k : params.keySet()) {
			Object v = params.get(k);
			if(v.getClass().isArray()) {
				Object [] arr = (Object []) v;
				if(arr != null && arr.length==1) {
					v = arr[0];
				}
			}
			
			if(k.startsWith("__")) {
				((Map<String, Object>)formData.get("ctx")).put(k.substring(2), v);
			} else {
				formData.put(k, v);
			}
		}
		
		try {
			Node sf = formNode.getNode().addNode(""+System.currentTimeMillis());
			sf.setProperty("type", "form.submit");
			Set<String> keys = formData.keySet();
			for(String k : keys) {
				if("ctx".equals(k)) {
					//skip ctx object
					continue;
				}
				sf.setProperty(k, (String)formData.get(k));
			}
			sf.getSession().save();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//formNode.getWorkflow().run(submittedData) ... 
		return formData;
	}

}
