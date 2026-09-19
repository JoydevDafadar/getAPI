package com.example.GetAPI.enums;

public enum TransactionEvent {
	
	PRE_TRANSACTION("PreTransaction"),
	POST_TRANSACTION("PostTransaction");
	
	private String method;
	
	TransactionEvent( String code ) {
		this.method = code;
	}
	
	public String getMethod() {
		return method;
	}
}
