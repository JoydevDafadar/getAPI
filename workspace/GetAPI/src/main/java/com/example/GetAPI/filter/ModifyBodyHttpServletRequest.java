package com.example.GetAPI.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

@Component
public class ModifyBodyHttpServletRequest extends HttpServletRequestWrapper {

	private final byte[] body;
	
	public ModifyBodyHttpServletRequest(
	    HttpServletRequest request,
	    String modifiedBody) {
	
		super(request);
		this.body = modifiedBody.getBytes(StandardCharsets.UTF_8);
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
	public BufferedReader getReader() {
		return new BufferedReader(
		        new InputStreamReader(
		                getInputStream(),
		                StandardCharsets.UTF_8));
	}
}