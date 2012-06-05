package org.chon.cms.light.mvc;

import org.chon.cms.light.mvc.result.Result;
import org.chon.cms.model.ContentModel;
import org.chon.web.api.Request;
import org.chon.web.api.Response;



public abstract class AbstractAction {
	private static final ResultCreator _R = new ResultCreator();
	
	/*injected*/
	protected Request req;
	protected Response resp;
	protected Object model;
	
	/*cached*/
	protected ResultCreator R = _R;
	
	/*the exec method*/
	public abstract Result exec();
	
	protected ContentModel getContentModel() {
		return (ContentModel) req.attr(ContentModel.KEY);
	}
}
