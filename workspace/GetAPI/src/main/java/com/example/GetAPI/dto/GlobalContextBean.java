package com.example.GetAPI.dto;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope( value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GlobalContextBean {

	private Map<String, Object> authContextMap;
	
	private Map<String, Object> reqContext;

	public Map<String, Object> getReqContext() {
		return reqContext;
	}

	public void setReqContext(Map<String, Object> reqContext) {
		this.reqContext = reqContext;
	}

	public Map<String, Object> getAuthContextMap() {
		return authContextMap;
	}

	public void setAuthContextMap(Map<String, Object> authContextMap) {
		this.authContextMap = authContextMap;
	}
	
}
