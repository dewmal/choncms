package org.chon.cms.admin.editor.ext.editors;

import org.chon.cms.admin.editor.model.NodeEditor;

public class HtmlNodeEditor implements NodeEditor {

	@Override
	public String getTemplate() {
		return "admin/editor/html/edit.html";
	}

}
