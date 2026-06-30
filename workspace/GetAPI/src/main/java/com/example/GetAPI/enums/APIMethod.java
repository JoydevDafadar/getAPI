package com.example.GetAPI.enums;

public enum APIMethod {

	GET("GET"),
	POST("POST"),
	PUT("PUT"),
	PATCH("PATCH"),
	DELETE("DELETE");
	
	private String method;
	
	APIMethod( String code ) {
		this.method = code;
	}
	
	public String getMethod() {
		return method;
	}
	
}
