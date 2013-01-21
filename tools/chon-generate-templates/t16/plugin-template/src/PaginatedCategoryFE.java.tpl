package ${project-package};

import java.util.ArrayList;
import java.util.List;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;

public class PaginatedCategoryFE {

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
	
	private Request req;

	public PaginatedCategoryFE(Request req) {
		this.req = req;
	}
	
	private static final String querytpl = "SELECT * FROM [nt:unstructured] " +
			"WHERE ISDESCENDANTNODE('%s') AND [type]='html' " +
			"AND [jcr:created] IS NOT NULL ORDER BY [jcr:created] DESC";
	
	
	private QueryResult query(IContentNode cat, int page, int limit) throws RepositoryException {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		String q = String.format(querytpl, cat.getAbsPath());
		QueryResult res = null;
		if(limit < 0) {			
			res = cm.query(q, Query.JCR_SQL2, null, null);
		} else {
			res = cm.query(q, Query.JCR_SQL2, page * limit, limit);
		}
		return res;
	}
	
	
	public List<IContentNode> getNodes(IContentNode cat, int page, int limit) throws RepositoryException {
		QueryResult res = query(cat, page-1, limit);
		NodeIterator ni = res.getNodes();
		List<IContentNode> rv = new ArrayList<IContentNode>();
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		while(ni.hasNext()) {
			rv.add(cm.getContentNode(ni.nextNode()));
		}
		return rv;
	}
	
	public long getTotalNodes(IContentNode cat) throws RepositoryException {
		QueryResult res = query(cat, 0, -1);
		return res.getRows().getSize();
	}
	
	public List<Page> getPages(IContentNode cat, int page, int pm_pages, int itemsOnOnePage) throws RepositoryException {
		List<Page> pages = new ArrayList<Page>();
		long total = 0;
		try {
			total = getTotalNodes(cat);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
