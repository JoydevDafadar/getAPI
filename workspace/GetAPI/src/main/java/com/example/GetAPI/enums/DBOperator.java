package com.example.GetAPI.enums;


public enum DBOperator {

	GTHN(">"),
	LTHN("<"),
	EQL("="),
	NEQL("<>"),
	LIKE("LIKE");
	
	
	private String code;
	
	private DBOperator(String code) {
		this.code = code;
	}
	
	public String getCode() {
        return code;
    }
	
}
