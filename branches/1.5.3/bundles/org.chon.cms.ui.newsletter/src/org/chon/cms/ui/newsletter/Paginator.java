package org.chon.cms.ui.newsletter;

import java.util.ArrayList;
import java.util.List;

public class Paginator {
	private int page; 
	private long totalItems;
	private int itemsOnOnePage;
	
	public Paginator(int page, long totalItems, int itemsOnOnePage) {
		if(page < 1) page = 1;
		//if(page > totalItems/itemsOnOnePage) page = totalItems/itemsOnOnePage;
		this.page = page;
		this.totalItems = totalItems;
		this.itemsOnOnePage = itemsOnOnePage;
	}


	public long getTotalItems() {
		return totalItems;
	}
	
	public int getPage() {
		return page;
	}
	
	public long getTotalPages() {
		long totalPages = totalItems / itemsOnOnePage;
		if(totalItems % itemsOnOnePage != 0)
			totalPages++;
		return totalPages;
	}


	public static class Page {
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
	

	public List<Page> getPages(int total) {
		List<Page> pages = new ArrayList<Page>();
		if (page > 1 && totalItems>0) {
			pages.add(new Page(page - 1, " << "));
		}
		long totalPages = getTotalPages();
		long startPage = 1, endPage = totalPages;
		if(total < endPage - startPage) {
			startPage = page - total/2;
			if(startPage < 1) startPage = 1;
			endPage = startPage + total - 1;
			while(endPage > totalPages) {
				startPage--;
				endPage--;
			}
		}
		for(long i=startPage; i<=endPage; i++) {
			pages.add(new Page((int) i, String.valueOf(i)));			
		}
		
		if (page < totalPages) {
			pages.add(new Page(page + 1, " >> "));
		}
		return pages;
	}


	public Integer getStart() {
		if(page<=1) return 0;
		long totalPages = getTotalPages();
		if(page > totalPages) page = (int) totalPages;
		return (page-1)*itemsOnOnePage;
	}


	public Integer getLimit() {
		return itemsOnOnePage;
	}
}
