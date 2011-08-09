package org.chon.cms.ui.lightbox.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.model.renderers.VTplNodeRenderer;
import org.chon.cms.core.model.types.ContentNode;
import org.chon.cms.core.model.types.FileContentNode;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class LightBox {
	private ContentNode node;
	private Response resp;
	private JCRApplication app;
	private Request req;

	public LightBox(Request req, Response resp, IContentNode node, JCRApplication app) throws Exception {
		if(node instanceof ContentNode) {
		this.node = (ContentNode) node;
		this.resp = resp;
		this.app = app;
		this.req = req;
		init(resp);
		} else {
			throw new Exception("Invalid node type, must be instance of ContentNode " + node.getClass());
		}
	}

	private void init(Response resp) {
		@SuppressWarnings("unchecked")
		List<String> scrips = (List<String>) resp.getTemplateContext().get("head:scripts");
		@SuppressWarnings("unchecked")
		List<String> css = (List<String>) resp.getTemplateContext().get("head:css");
		
		css.add("lightbox/css/lightbox.css");
		
		scrips.add("lightbox/js/prototype.js");
		scrips.add("lightbox/js/scriptaculous.js?load=effects,builder");
		scrips.add("lightbox/js/lightbox.js");
	}
	
	/**
	 * Renders lightbox styled images
	 * puts into one group (the name of current node)
	 * eg.
	 * $ext.lightbox.render("image_name.jpg:Image Description", "image2:Description 2")
	 * 
	 * @param imgs
	 * @return
	 * @throws PathNotFoundException
	 * @throws RepositoryException
	 */
	public String render(String [] imgs) throws PathNotFoundException, RepositoryException {
		List<LightboxImageNode> images = new ArrayList<LightboxImageNode>();
		for(String img : imgs) {
			String [] img_descr = img.split(":");
			if(img_descr.length != 2) {
				img_descr = new String []{img, img};
			}
			LightboxImageNode imgNode = new LightboxImageNode(node.getPath() + "/" + img_descr[0], img_descr[1]);
			images.add(imgNode);
		}
		return renderImages(images);
	}
	
	/**
	 * Renders all child images for current node
	 * 
	 * @return
	 */
	public String renderImages() {
		List<LightboxImageNode> lb_images = new ArrayList<LightboxImageNode>();
		List<FileContentNode> images = node.getImages();
		for(FileContentNode img : images) {
			lb_images.add(new LightboxImageNode(img.getPath(), img.getName()));
		}
		return renderImages(lb_images);
	}
	
	
	private String renderImages(List<LightboxImageNode> images) {
		Map<String, Object> params = new HashMap<String, Object>();
		VTplNodeRenderer.prepareParams(node, req, resp, params, app);
		params.put("images", images);
		return resp.formatTemplate("lightbox/render.html", params);
	}
}
