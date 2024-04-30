package com.example.utils;

public class PageView implements java.io.Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final long MAX_PAGE_SIZE = 500;

	/**
	 * 当前页码
	 */
	private long pageIndex = 1; // 当前页码

	/**
	 * 每页显示多少条
	 */
	private long pageSize = 20; // 每页显示多少条

	/**
	 * 一共多少条数据
	 */
	private long totalSize; // 一共多少条数据

	/**
	 * 一共多少页
	 */
	private long totalPage; // 一共多少页

	/**
	 * 是否分页 0分页　１不分页
	 */
	private int usePage=0;		//是否分页 0分页　１不分页

	/**
	 * 分页数据
	 */
	private Object result;


	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getUsePage() {
		return usePage;
	}

	public void setUsePage(int usePage) {
		this.usePage = usePage;
	}

	public long getStart() {
		return (this.pageIndex-1) * this.pageSize <= 0 ? 0 : (this.pageIndex-1) * this.pageSize;
	}

	public long getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(long pageIndex) {
		this.pageIndex = pageIndex;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		if(pageSize > MAX_PAGE_SIZE) {
			this.pageSize = MAX_PAGE_SIZE;
		} else {
			this.pageSize = pageSize;
		}
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
		if(totalSize%pageSize >= 1) {
			this.totalPage = (totalSize/pageSize)+1;
		} else {
			this.totalPage = (totalSize/pageSize);
		}
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public static void main(String[] args) {
		PageView pageView = new PageView();
		pageView.setTotalSize(21);
	}
}
