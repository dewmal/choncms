package org.chon.cms.core.model.types;

import java.util.Map;

import javax.jcr.Node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.core.model.renderers.VTplNodeRenderer;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;

/**
 * Html Content node that can contain velocity template in htmlText property
 *  {@link VTplNodeRenderer} will call eval before rendering
 *  
 * @author Jovica
 *
 */
public class EvalVelocityHtmlContentNode extends ContentNode {
	private static final Log log = LogFactory.getLog(EvalVelocityHtmlContentNode.class);
	
	private String htmlText = null;

	public EvalVelocityHtmlContentNode(ContentModel model, Node node,
			IContentNode typeDesc) {
		super(model, node, typeDesc);
	}

	public void eval(Application app, Map<String, Object> params) {
		try {
			String pHtmlText = prop("htmlText");
			if(pHtmlText != null) {
				htmlText = app.getTemplate().formatStr(pHtmlText, params, this.getName());
			} else {
				log.warn("Node " + this.getPath() + " does not have property htmlText");
			}
		} catch (Exception e) {
			htmlText = "Error evaluating template " + e.getMessage();
			log.error(htmlText, e);
		}
	}

	public String getHtmlText() {
		return htmlText;
	}
}
