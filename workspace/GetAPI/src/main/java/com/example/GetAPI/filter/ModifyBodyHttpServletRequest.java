package com.example.GetAPI.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;


public class ModifyBodyHttpServletRequest extends HttpServletRequestWrapper {

	private final byte[] body;
	
	private final Map<String, String[]> params;
	
	public ModifyBodyHttpServletRequest(
	    HttpServletRequest request,
	    String modifiedBody,
	    Map<String, String[]> modifiedParams ) {
	
		super(request);
		this.body = modifiedBody.getBytes(StandardCharsets.UTF_8);
		this.params = modifiedParams;
	}
	
	@Override
	public ServletInputStream getInputStream() {
	
	ByteArrayInputStream bis =
	        new ByteArrayInputStream(body);
	
	return new ServletInputStream() {
	
	    @Override
	    public int read() {
	        return bis.read();
	    }
	
	    @Override
	    public boolean isFinished() {
	        return bis.available() == 0;
	    }
	
	    @Override
	    public boolean isReady() {
	        return true;
	    }
	
	    @Override
	    public void setReadListener(ReadListener listener) {
	    }
	};
	}
	
	@Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        return values == null ? null : values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        return params.get(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(params);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }
	
	@Override
	public BufferedReader getReader() {
		return new BufferedReader(
		        new InputStreamReader(
		                getInputStream(),
		                StandardCharsets.UTF_8));
	}
}