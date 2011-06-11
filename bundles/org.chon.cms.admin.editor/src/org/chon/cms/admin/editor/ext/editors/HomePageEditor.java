package org.chon.cms.admin.editor.ext.editors;

import org.chon.cms.admin.editor.model.NodeEditor;

public class HomePageEditor implements NodeEditor {

	@Override
	public String getTemplate() {
		return "admin/editor/homePage/edit.html";
	}

}
