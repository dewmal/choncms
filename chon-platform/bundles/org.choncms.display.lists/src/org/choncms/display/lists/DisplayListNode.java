package org.choncms.display.lists;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

import org.chon.cms.core.model.types.ContentNode;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.json.JSONArray;
import org.json.JSONException;

public class DisplayListNode extends ContentNode {
	
	public static final String DISPLAY_LIST_TYPE = "display.list";
	
	public DisplayListNode(ContentModel model, Node node, IContentNode typeDesc) {
		super(model, node, typeDesc);
	}
	
	public String getListType() {
		return this.prop("listType");
	}
	
	public List<IContentNode> getItems() {
		if("simple".equals(getListType())) {
			try {
				return getSimpleListNodes();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new ArrayList<IContentNode>();
	}
	
	private List<IContentNode> getSimpleListNodes() throws JSONException {
		List<IContentNode> rv = new ArrayList<IContentNode>();
		JSONArray arr = new JSONArray( this.prop("items") );
		for(int i=0; i<arr.length(); i++) {
			rv.add(this.getContentModel().getContentNode(arr.getString(i)));
		}
		return rv;
	}

	public long getItemsSize() {
		return getItems().size();
	}

}
