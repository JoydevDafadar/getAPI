package com.example.GetAPI.utility;

import java.security.Timestamp;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatusCode;

public class ResponseWrapper {
	
	private String url;
	private Float responseTime;
	private HttpStatusCode statusCode;
	private Object data;
	private Object exception;
	private LocalDateTime time;
	
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Float getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Float responseTime) {
		this.responseTime = responseTime;
	}
	public HttpStatusCode getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(HttpStatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Object getException() {
		return exception;
	}
	public void setException(Object exception) {
		this.exception = exception;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	
	
	
}
