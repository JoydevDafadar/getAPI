package com.example.GetAPI.enums;

public enum TransactionType {

	VALIDATION("Validation"),
	TRANSACTION("Transaction");
	
	private String method;
	
	TransactionType( String code ) {
		this.method = code;
	}
	
	public String getMethod() {
		return method;
	}
	
}
