package org.chon.cms.admin.editor;

import java.util.Hashtable;

import org.chon.cms.admin.editor.ext.EditorExtension;
import org.chon.cms.admin.editor.ext.editors.CategoryNodeEditor;
import org.chon.cms.admin.editor.ext.editors.HomePageEditor;
import org.chon.cms.admin.editor.ext.editors.HtmlNodeEditor;
import org.chon.cms.admin.editor.model.NodeEditor;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected String getName() {
		return "org.chon.cms.admin.editor";
	}

	@Override
	protected void registerExtensions(JCRApplication app) {
		regTypeEditor("html", new HtmlNodeEditor());
		regTypeEditor("category", new CategoryNodeEditor());
		regTypeEditor("sys.public", new HomePageEditor());
		
		app.regExtension("org.chon.cms.admin.editor", new EditorExtension(this.getBundleContext()));
	}

	private void regTypeEditor(String type, NodeEditor nodeEditor) {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("type", type);
		this.getBundleContext().registerService(NodeEditor.class.getName(), nodeEditor, props);
	}

}
