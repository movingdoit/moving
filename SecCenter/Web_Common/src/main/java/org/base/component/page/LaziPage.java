package org.base.component.page;

import java.util.List;


public class LaziPage<T> {
	/**
	 * 总记录数
	 */
	private int total;
	
	/**
	 * 当前页
	 */
	private int pageNo;
	/**
	 * 分页大小（记录数）
	 */
	private int pageSize;
	/**
	 * 当前分页中数据
	 */
	private List<T> rows;

	public LaziPage() {
		super();
	}
	public LaziPage(PageFinder<T> pageFinder){
		this.total = pageFinder.getRowCount();
		this.pageNo = pageFinder.getPageNo();
		this.pageSize = pageFinder.getPageSize();
		this.rows = pageFinder.getData();
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
