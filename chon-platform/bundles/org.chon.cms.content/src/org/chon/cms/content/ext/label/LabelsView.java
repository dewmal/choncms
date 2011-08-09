package org.chon.cms.content.ext.label;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.model.content.IContentNode;


public class LabelsView {
	
	private static final Log log = LogFactory.getLog(LabelsView.class);
	
	private IContentNode node;

	public LabelsView(IContentNode contentNode) {
		this.node = contentNode;
	}

	public String get(String key) {
		if(log.isTraceEnabled()) {
			log.trace("Getting Label: " + key);
		}
		String l = node.prop("label." + key);
		if(l == null) {
			return "Label['"+key+"']#!NOT-DEFINED!#";
		}
		return l;
	}
}
