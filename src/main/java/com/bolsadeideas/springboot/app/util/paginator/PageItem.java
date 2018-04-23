package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

public class PageItem {
	
	private int number;
	private boolean current;
	private List<PageItem> pages;
	
	public PageItem(int number, boolean current) {
		this.number = number;
		this.current = current;
		this.pages = new ArrayList<>();
	}

	public int getNumber() {
		return number;
	}

	public boolean isCurrent() {
		return current;
	}
	
}
