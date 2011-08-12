package org.chon.cms.core.model.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.jcr.Node;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.PropertyType;

/**
 * Node that have html nodes... Can contain category as well
 * 
 * @author Jovica
 *
 */
public class CategoryContentNode extends ContentNode {
	public CategoryContentNode(ContentModel model, Node node,
			IContentNode typeDesc) {
		super(model, node, typeDesc);
	}
	
	
	public List<IContentNode> getChilds(int start, int limit, String order) {
		String [] fo = order.split(" ");
		final String field = fo[0].trim();
		final boolean desc = fo[1].trim().equals("DESC");
		
		List<IContentNode> childs = super.getChilds(start, limit);
		if(childs != null) {
			//order
			Collections.sort(childs, new Comparator<IContentNode>() {
				@Override
				public int compare(IContentNode n1, IContentNode n2) {
					Long o1 = (Long) n1.getPropertyAs(field, PropertyType.LONG);
					if(o1 == null) o1=0L;
					Long o2 = (Long) n2.getPropertyAs(field, PropertyType.LONG);
					if(o2 == null) o2=0L;
					int rv = (int)(o2-o1); 
					return desc ? rv : -1*rv;
				}
			});
		}
		return childs;
	}
	
	@Override
	public List<IContentNode> getChilds(int start, int limit) {
		return getChilds(start, limit, "order ASC");
	}


	public class Page {
		private int num;
		private String title;

		public Page(int num, String title) {
			super();
			this.num = num;
			this.title = title;
		}

		public int getNum() {
			return num;
		}

		public String getTitle() {
			return title;
		}
	}
	
	public List<Page> getPages(int page, int pm_pages, int itemsOnOnePage) {
		List<Page> pages = new ArrayList<Page>();
		long total = getChildsSize();
		if (page > 1 && total>0) {
			pages.add(new Page(page - 1, " << "));
		}
		int start = page - pm_pages;
		int end = page + pm_pages;
		if (start < 1)
			start = 1;
		long totalPages = total / itemsOnOnePage;
		if(total % itemsOnOnePage != 0)
			totalPages++;
		
		if (end > totalPages)
			end = (int) totalPages;
		for (int i = start; i <= end; i++) {
			pages.add(new Page(i, String.valueOf(i)));
		}
		if (page * itemsOnOnePage < total) {
			pages.add(new Page(page + 1, " >> "));
		}
		return pages;
	}
}
