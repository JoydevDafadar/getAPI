package com.example.GetAPI.enums;

public enum Datatypes {

	INT(1),
	FLT(2),
	STR(3);
	
	private int code;
	
	private Datatypes(int code) {
		this.code = code;
	}
	
	public int getCode() {
        return code;
    }
	
}
