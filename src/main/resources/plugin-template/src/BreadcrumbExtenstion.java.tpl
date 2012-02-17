package ${project-package};

import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public class BreadcrumbExtenstion implements Extension {

	@Override
	public Map<String, Action> getAdminActons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		// getTplObject is called once per page render (only if this extenstion is used in the page)
		// return new object passed to template
		// $ext.breadcrumb.list will call com.test.BreadcrumbFE.getList()
		return new BreadcrumbFE(req, resp, node);
	}

}
