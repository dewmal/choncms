package org.chon.cms.admin.editor.ext.actions;

import java.util.List;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.chon.cms.core.model.types.ContentNode;
import org.chon.cms.core.model.types.FileContentNode;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONArray;

public class AjaxAxtionMCEImages implements Action {
	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		String id = req.get("id");
		try {
			Node node = cm.getSession().getNodeByIdentifier(id);
			IContentNode cNode = cm.getContentNode(node);
			if (cNode instanceof ContentNode) {
				ContentNode c = (ContentNode) cNode;
				List<FileContentNode> images = c.getImages();
				return toTinyMCEJSString(images);
			} else {
				throw new RuntimeException("Invalid node type");
			}
		} catch (ItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	private String toTinyMCEJSString(List<FileContentNode> images) {
		StringBuffer sb = new StringBuffer();
		// sb.append("var showPreviewImage = ImageDialog.showPreviewImage;\n");
		// sb.append("ImageDialog.showPreviewImage = function(img) {\n");
		// String base = app.getAppProperties().getProperty("siteUrl") +
		// node.getPath() + "/";
		// sb.append("		showPreviewImage.call(this, '"+base+"' + img);\n");
		// sb.append("}\n\n");

		sb.append("var tinyMCEImageList = ");
		JSONArray arr = new JSONArray();

		for (int i = 0; i < images.size(); i++) {
			String imgNodeName = images.get(i).getName();
			JSONArray img = new JSONArray();
			// img.put(imgNodeName + " - " + " Мала");
			// img.put(imgNodeName + "?w=80&h=60");
			// arr.put(img);
			//
			// img = new JSONArray();
			// img.put(imgNodeName + " - " + " Средна");
			// img.put(imgNodeName + "?w=160&h=120");
			// arr.put(img);
			//
			// img = new JSONArray();
			// img.put(imgNodeName + " - " + " Голема");
			img.put(imgNodeName);
			img.put(imgNodeName + "?w=320&h=240");
			arr.put(img);

			// img = new JSONArray();
			// img.put(imgNodeName + " - " + " Оригинал");
			// img.put(imgNodeName);
			// arr.put(img);
		}
		sb.append(arr.toString());
		return sb.toString();
	}
}
