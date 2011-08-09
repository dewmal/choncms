package org.chon.cms.menu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chon.cms.menu.model.factory.MiFactory;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.PropertyType;

public abstract class BaseMenuItem implements IMenuItem {
	
	protected IContentNode node = null;
	private IMenuItem parent = null;
	
	private MiFactory factory = null;
	private Map<String, ? extends IMenuItem> childs = null;
	
	
	public BaseMenuItem(IContentNode node, IMenuItem parent, MiFactory factory) {
		this.parent = parent;
		this.node = node;
		this.factory = factory;
	}
	
	@Override
	public IMenuItem getChild(String name) {
		if(childs == null) {
			getChilds();
		}
		if(childs == null) {
			return null;
		}
		return childs.get(name);
	}

	
	@Override
	public List<IMenuItem> getChilds() {
		List<IMenuItem> rv = null;
		if(childs != null) {
			rv = new ArrayList<IMenuItem>(childs.values());
		} else {
			childs = createChilds();
			if(childs != null) {
				rv = new ArrayList<IMenuItem>(childs.values());
			}
		}
		
		if(rv != null) {
			Collections.sort(rv, new Comparator<IMenuItem>() {
				@Override
				public int compare(IMenuItem a, IMenuItem b) {
					return a.getOrder() - b.getOrder();
				}
			});
		}
		return rv;
	}

	private Map<String, ? extends IMenuItem> createChilds() {
		List<IContentNode> nodeChilds = node.getChilds();
		Map<String, IMenuItem> miChilds = new HashMap<String, IMenuItem>();
		if(nodeChilds != null) {
			for(IContentNode nc : nodeChilds) {
				IMenuItem mi = factory.createMenuItem(nc, this);
				if(mi != null) {
					miChilds.put(nc.getName(), mi);
				}
			}
		}
		return miChilds;
	}

	@Override
	public List<IMenuItem> getChilds(String path) {
		if(path==null) {
			return null;
		}
		
		while(path.startsWith("/")) {
			path = path.substring(1);
		}
		while(path.endsWith("/")) {
			path = path.substring(0, path.length()-1);
		}
		
		if(path.contains("/")) {	
			//path.split("/")
			String name = path.substring(0, path.indexOf("/"));
			return getChild(name).getChilds(path.substring(path.indexOf("/")+1));
		}
		
		IMenuItem mi = getChild(path);
		if(mi != null) {
			return mi.getChilds();
		}
		return null;
	}
	
	@Override
	public String getFullLink() {
		if(this.isInternalLink()) {
			String siteUrl = node.getContentModel().getConfigNode("host.config").prop("siteUrl");
			return siteUrl + "/" + getLink();
		}
		return getLink();
	}
	
	@Override
	public String getTitle() {
		String title = node.prop("title");
		if(title == null) {
			title = node.getName();
		}
		return title;
	}
	
	@Override
	public String getPath() {
		IMenuItem p = this.getParent();
		if(p != null && p.getParent() != null) {
			return p.getPath() + "/" + this.getName();
		}
		return this.getName();
	}

	@Override
	public IMenuItem getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return node.getName();
	}

	@Override
	public int getOrder() {
		Long a = (Long) node.getPropertyAs("order", PropertyType.LONG);
		if(a != null) {
			return a.intValue();
		}
		return 0;
	}
}
