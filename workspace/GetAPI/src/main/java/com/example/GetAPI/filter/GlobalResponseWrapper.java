package com.example.GetAPI.filter;

import java.time.LocalDateTime;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.GetAPI.utility.ResponseWrapper;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

    	
        ServletServerHttpRequest servletRequest =
                (ServletServerHttpRequest) request;
        
        ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;

        HttpServletRequest req = servletRequest.getServletRequest();

        long startTime = (long) req.getAttribute("START_TIME");
        float responseTime =
                (System.currentTimeMillis() - startTime);

        
        HttpStatusCode statusCode = HttpStatusCode.valueOf
        		(servletResponse.getServletResponse().getStatus());
        
        ResponseWrapper wrapper = new ResponseWrapper();
        
        wrapper.setUrl(req.getRequestURI());
        wrapper.setResponseTime(responseTime);
        wrapper.setStatusCode(statusCode);
        wrapper.setTime(LocalDateTime.now());
        
        if( (statusCode != null && !statusCode.isError()) ) {
        	wrapper.setData(body);
        	wrapper.setException(null);
        }
        else {
            wrapper.setData(null);
            wrapper.setException(body);
        }
        
        
        return wrapper;
    }

}