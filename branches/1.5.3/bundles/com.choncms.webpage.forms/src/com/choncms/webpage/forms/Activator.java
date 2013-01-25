package com.choncms.webpage.forms;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;

import org.chon.cms.content.utils.ChonTypeUtils;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.core.model.types.ContentNode;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.osgi.framework.BundleContext;

import com.choncms.webpage.forms.ext.FormsExtension;
import com.choncms.webpage.forms.renderers.AjaxFormSubmitRenderer;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected void registerExtensions(JCRApplication app) {
		
		try {
			WorkflowUtils.init(getBundleContext(), app, getConfig());
			
			String appFormDataNodeName = getConfig().getString("appFormDataNode");
			ContentModel cm = app.createContentModelInstance(getName());
			IContentNode appFormDataNode = cm.getAppsConfigNode(appFormDataNodeName, true);
			
			String ajaxFormSubmitNode = getConfig().getString("ajaxFormSubmitNode");
			try {
				ChonTypeUtils.registerNodeRenderer(getBundleContext(),
						AjaxFormSubmitRenderer.class);
				
				createAjaxFormSubmitNode(cm, appFormDataNode.getAbsPath(), ajaxFormSubmitNode);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			app.regExtension("forms", new FormsExtension(app, getName(), appFormDataNode, ajaxFormSubmitNode));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		WorkflowUtils.close();
		super.stop(context);
	}


	private static final String AJAX_FORM_SUBMIT_NODE_TYPE = "submit.from.ajax.type";
	
	private void createAjaxFormSubmitNode(ContentModel cm, String formDataNodeAbsPath, String ajaxFormSubmitNode) throws Exception {
		Map<String, String> properties = new HashMap<String, String>();
		Map<String, String> defaultNodeProperties = new HashMap<String, String>();
		ChonTypeUtils.registerType(cm, AJAX_FORM_SUBMIT_NODE_TYPE, ContentNode.class, AjaxFormSubmitRenderer.class, properties, defaultNodeProperties);
		Node wwwNode = cm.getPublicNode().getNode();
		Node n;
		if(wwwNode.hasNode(ajaxFormSubmitNode)) {
			n = wwwNode.getNode(ajaxFormSubmitNode);
		} else {
			n = wwwNode.addNode(ajaxFormSubmitNode);
		}
		n.setProperty("type", AJAX_FORM_SUBMIT_NODE_TYPE);
		n.setProperty("formDataNodeAbsPath", formDataNodeAbsPath);
		n.getSession().save();
	}

	@Override
	protected String getName() {
		return "com.choncms.webpage.forms";
	}
	
}
