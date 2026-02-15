package com.example.GetAPI.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ResponseModifierFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		try {
			
			System.out.println("Incoming request: " + req.getRequestURI());
			
			long startTime = System.currentTimeMillis();
	        req.setAttribute("START_TIME", startTime);

	        
	        chain.doFilter(request, response); 
	        
	        System.out.println("Outgoing response status: " + res.getStatus());
			
		} catch (Exception e) {
		    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.setContentType("application/json");

            res.getWriter().write("""
                {
                  "error": "INTERNAL_ERROR",
                  "message": "Servlet Level Exception."
                }
            """);
		}
		
	}
	
	

}
