package org.chon.cms.admin.editor.ext;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.admin.editor.ext.actions.AjaxActionContentPublish;
import org.chon.cms.admin.editor.ext.actions.AjaxAxtionMCEImages;
import org.chon.cms.admin.editor.ext.editors.ActionContentEdit;
import org.chon.cms.core.Extension;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.osgi.framework.BundleContext;

public class EditorExtension implements Extension {


	private Map<String, Action> actions = new HashMap<String, Action>();
	Map<String, Action> ajaxActions = new HashMap<String, Action>();

	public EditorExtension(BundleContext bundleContext) {
		actions.put("content.edit", new ActionContentEdit(bundleContext));
		ajaxActions.put("content.publish", new AjaxActionContentPublish());
		ajaxActions.put("content.mce.images", new AjaxAxtionMCEImages());
	}

	@Override
	public Map<String, Action> getAdminActons() {
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		return ajaxActions;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
