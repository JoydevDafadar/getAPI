package com.example.GetAPI.enums;

public enum Constraints {
	
	PKEY( "PKEY" ),
	UNQ( "UNQ" );
	
	private String code;
	
	private Constraints( String code ) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}

}
