package ${project-package};

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.chon.cms.core.model.types.RootContentNode;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class BreadcrumbFE {

	private IContentNode node;

	public BreadcrumbFE(Request req, Response resp, IContentNode node) {
		this.node = node;
	}
	
	public List<IContentNode> getList() {
		List<IContentNode> rv = new ArrayList<IContentNode>();
		if(node instanceof RootContentNode) {
			return rv;
		}
		IContentNode n = node.getParent();
		while(n != null) {
			rv.add(n);
			if(n instanceof RootContentNode) 
				break;
			n = n.getParent();
		}
		Collections.reverse(rv);
		return rv;
	}

}
