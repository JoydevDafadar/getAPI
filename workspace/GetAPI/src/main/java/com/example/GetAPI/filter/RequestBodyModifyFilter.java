package com.example.GetAPI.filter;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ContextExposingHttpServletRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class RequestBodyModifyFilter
        extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String body =
                request.getReader()
                       .lines()
                       .collect(Collectors.joining());

        ObjectMapper mapper = new ObjectMapper();

        Map<String,Object> json =
                mapper.readValue(body, Map.class);

        json.put("modifiedBy", "JWT_FILTER");

        String modifiedBody =
                mapper.writeValueAsString(json);

        ModifyBodyHttpServletRequest wrapped =
                new ModifyBodyHttpServletRequest(
                        request,
                        modifiedBody);	

        filterChain.doFilter(wrapped, response);
    }
}