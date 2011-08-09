package org.chon.cms.content.ext.news;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;


public class NewsView {
	private static final String NEWS_NODE = "www/news";
	
	private ContentModel cm;
	

	public NewsView(ContentModel cm) {
		this.cm = cm;
	}

	public List<IContentNode> getList() {
		return getLatest(6);
	}
	
	public List<IContentNode> getLatest(int limit) {
		List<IContentNode> list=new ArrayList<IContentNode>();
		try {
			QueryResult r = cm.query(
					"select * from [nt:unstructured] as x " +
					"WHERE isdescendantnode(x, '/"+NEWS_NODE+"') AND [type]='html' " +
					"ORDER BY [jcr:created] DESC", Query.JCR_SQL2, 0, limit);
			NodeIterator ni = r.getNodes();
			while(ni.hasNext()) {
				list.add(cm.getContentNode(ni.nextNode()));
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
