package br.com.ericsson.smartnotification.web.domain;

public class PageableView {
       
	private int current;
	
	private int totalItemsPerPage;
	
	private long totalItens;

	public PageableView() {
		this.current = 1;
		this.totalItemsPerPage = 5;
	}

	public PageableView(int  totalItens) {
		this.current = 1;
		this.totalItemsPerPage = 5;
		this.totalItens = totalItens;
	}
	
	public PageableView(int current, int totalItemsPerPage, long totalItens, String urlbase) {
		super();
		this.current = current;
		this.totalItemsPerPage = totalItemsPerPage;
		this.totalItens = totalItens;
	}

	public int getCurrent() {
		return current;
	}

	public long getTotalPages() {
		return (long) Math.ceil((double)totalItens / totalItemsPerPage);
	}
	
	public void setCurrent(int current) {
		this.current = current;
	}

	public int getTotalItemsPerPage() {
		return totalItemsPerPage;
	}

	public void setTotalItemsPerPage(int totalItemsPerPage) {
		this.totalItemsPerPage = totalItemsPerPage;
	}

	public long getTotalItens() {
		return totalItens;
	}

	public void setTotalItens(long totalItens) {
		this.totalItens = totalItens;
	}

}
