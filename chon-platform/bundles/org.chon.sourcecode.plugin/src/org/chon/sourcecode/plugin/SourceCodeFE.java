package org.chon.sourcecode.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chon.cms.core.model.types.FileContentNode;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Response;

public class SourceCodeFE {
	private IContentNode node;
	private Response resp;

	public SourceCodeFE(IContentNode node, Response resp) {
		this.node = node;
		this.resp = resp;
		init(resp);
	}
	
	private void init(Response resp) {
		@SuppressWarnings("unchecked")
		List<String> scrips = (List<String>) resp.getTemplateContext().get("head:scripts");
		@SuppressWarnings("unchecked")
		List<String> css = (List<String>) resp.getTemplateContext().get("head:css");
		
		css.add("syntaxhighlighter_3.0.83/styles/shCore.css");
		css.add("syntaxhighlighter_3.0.83/styles/shDefault.css");
		css.add("syntaxhighlighter_3.0.83/styles/shThemeDefault.css");
		//css.add("syntaxhighlighter_3.0.83/styles/shCoreEclipse.css");
		//css.add("syntaxhighlighter_3.0.83/styles/shThemeEclipse.css");
		
		
		scrips.add("syntaxhighlighter_3.0.83/scripts/shCore.js");
		scrips.add("syntaxhighlighter_3.0.83/scripts/shBrushJava.js");
	}

	/**
	 * Show source code using SyntaxHighliter
	 * 
	 * TODO: for now we have java only, make this work for other syntax
	 * make detection on syntax based on 
	 * @param name
	 * @return
	 */
	public String show(String name) {
		IContentNode file =  node.getChild(name);
		if(file instanceof FileContentNode) {
			FileContentNode f = (FileContentNode) file;
			//HOW TO GET FILE DATA?
			System.out.println(f);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("lang", "java");
			params.put("file", f);
			return resp.formatTemplate("sourcecode/tpl.html", params);
		}
		return "Invalid File";
	}
}
