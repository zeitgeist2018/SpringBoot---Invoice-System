package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;
	private int totalPages;
	private int itemsPerPage;
	private int currPage;
	private List<PageItem> pages;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<>();

		this.itemsPerPage = page.getSize();
		this.totalPages = page.getTotalPages();
		this.currPage = page.getNumber() + 1;

		int from, until;
		if(totalPages <= itemsPerPage) {
			from = 1;
			until = totalPages;
		} else {
			if(currPage <= (itemsPerPage / 2)) {
				from = 1;
				until = itemsPerPage;
			} else if(currPage >= totalPages - itemsPerPage / 2) {
				from = totalPages - itemsPerPage + 1;
				until = itemsPerPage;
			} else {
				from = currPage - (itemsPerPage / 2);
				until = itemsPerPage;
			}
		}

		for(int i=0;i<until;i++) {
			pages.add(new PageItem(from + i, currPage == from + i));
		}
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

	public String getUrl() {
		return url;
	}

	public Page<T> getPage() {
		return page;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public List<PageItem> getPages() {
		return pages;
	}



}
