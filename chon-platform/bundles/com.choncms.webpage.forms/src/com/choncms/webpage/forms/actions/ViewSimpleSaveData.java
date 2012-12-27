package com.choncms.webpage.forms.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.json.XML;

public class ViewSimpleSaveData extends AbstractFormsAction {

	private SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public class Utils {
		public String escapeHTML(String s) {
			return XML.escape(s);
		}
		
		public String formatStr(String s) {
			if(s==null) return "n/a";
			if(s != null) {
				s = escapeHTML(s);
				s = s.replaceAll("\n", "<br />");
			}
			return s;
		}
		
		public String formatDate(Calendar date) {
			if(date==null) return "n/a";
			return SDF.format(date.getTime());
		}
	}
	
	public ViewSimpleSaveData(String prefix, IContentNode appFormDataNode) {
		super(prefix, appFormDataNode);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		String formName = req.get("name");
		IContentNode formNode = appFormDataNode.getChild(formName);

		List<IContentNode> nodes = filterByType(formNode.getChilds(),
				"form.submit");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nodes", nodes);
		params.put("utils", new Utils());
		
		try {
			params.put("properties", getCommonPropetiesNames(nodes));
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp.formatTemplate(prefix + "/viewData.html", params);
	}

	private String propsToSkip[] = new String[] { "id", "type",
			"jcr:primaryType", "jcr:created" };

	private List<String> getCommonPropetiesNames(List<IContentNode> nodes)
			throws RepositoryException {
		Set<String> rv = new HashSet<String>();
		for (IContentNode n : nodes) {
			PropertyIterator pi = n.getNode().getProperties();
			while (pi.hasNext()) {
				Property p = pi.nextProperty();
				String name = p.getName();
				if (contains(name, propsToSkip)) {
					continue;
				}
				rv.add(name);
			}
		}
		return new ArrayList<String>(rv);
	}

	private boolean contains(String name, String[] ls) {
		for (String s : ls) {
			if (name.equals(s)) {
				return true;
			}
		}
		return false;
	}

	private List<IContentNode> filterByType(List<IContentNode> childs, String t) {
		List<IContentNode> rv = new ArrayList<IContentNode>();
		for (IContentNode n : childs) {
			if (t.equals(n.getType())) {
				rv.add(n);
			}
		}
		return rv;
	}

}
