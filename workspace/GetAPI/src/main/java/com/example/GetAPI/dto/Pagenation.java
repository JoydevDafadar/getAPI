package com.example.GetAPI.dto;

public class Pagenation {
	
	private Integer pageNumber = 1;
	private Integer rowCount = 10;
	private String orderBy = "RID";
	
	
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		if( pageNumber > 0 ) this.pageNumber = pageNumber;
	}
	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount(Integer rowCount) {
		if( rowCount > 0 ) this.rowCount = rowCount;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
}
