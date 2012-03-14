package org.chon.cms.content.utils;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;

/**
 * Helper class for getting paginated list of nodes 						<br />
 * from JCR_SQL2 query														<br />
 * 																			<br />
 * pq = new PaginatedQuery(cm, "select * from [nt:unstructured]")			<br />
 * 																			<br />
 * Get the first page info (for 10 items per page):							<br />
 * paginator = pq.getPagiator(1, 10);										<br />	
 * 																			<br />	
 * Finally get the items:													<br />	
 * nodes = pq.getItems(paginator);											<br />
 * 				
 * @author Jovica
 */
public class PaginatedQuery {
	private ContentModel contentModel;
	private String query;
	
	public PaginatedQuery(ContentModel contentModel, String query) {
		this.contentModel = contentModel;
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public Paginator getPaginator(int page, int itemsOnOnePage) throws RepositoryException {		
		QueryResult qr = contentModel.query(getQuery(), Query.JCR_SQL2, null, null);
		NodeIterator ni = qr.getNodes();
		return new Paginator(page, ni.getSize(), itemsOnOnePage);
	}

	public List<IContentNode> getItems(Paginator p) throws RepositoryException {
		return getItems(p.getStart(), p.getLimit());
	}

	public List<IContentNode> getItems(int start, int limit) throws RepositoryException {
		QueryResult qr = contentModel.query(getQuery(), Query.JCR_SQL2, start, limit);
		NodeIterator ni = qr.getNodes();
		List<IContentNode> rv = new ArrayList<IContentNode>();
		while (ni.hasNext()) {
			IContentNode n = contentModel.getContentNode(ni.nextNode());
			rv.add(n);
		}
		return rv;
	}
}
