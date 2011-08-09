package org.chon.cms.content.ext.label.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.content.ext.label.LabelsExtension;
import org.chon.cms.model.ContentModel;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;


public class Action_labels implements Action {
	private static final Log log = LogFactory.getLog(Action_labels.class);

	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		try {
			List<Map<String, String>> labelList = new ArrayList<Map<String, String>>();
			Node labelsNode = cm.getAppsConfigNode(LabelsExtension.NODE_NAME, true).getNode();
			PropertyIterator it = labelsNode.getProperties();
			while (it.hasNext()) {
				Property p = it.nextProperty();
				String name = p.getName();
				if (name.startsWith("label.")) {
					Map<String, String> label = new HashMap<String, String>();
					label.put("name", name.substring(6));
					label.put("value", p.getString());
					labelList.add(label);
				}
			}
			Collections.sort(labelList, new Comparator<Map<String, String>>() {
				@Override
				public int compare(Map<String, String> o1,
						Map<String, String> o2) {
					return o1.get("name").compareTo(o2.get("name"));
				}
			});
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("labels", labelList);
			params.put("node", cm.getAppsConfigNode(LabelsExtension.NODE_NAME));
			return resp.formatTemplate("admin/labels.html", params);
		} catch (RepositoryException e) {
			log.error("Error while getting labels", e);
		}
		return null;
	}
}