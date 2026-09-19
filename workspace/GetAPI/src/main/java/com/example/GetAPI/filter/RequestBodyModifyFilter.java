package com.example.GetAPI.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ContextExposingHttpServletRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.GetAPI.dao.TAuthApiMapping;
import com.example.GetAPI.dao.User;
import com.example.GetAPI.dto.GlobalContextBean;
import com.example.GetAPI.repository.TAuthApiMappingRepository;
import com.example.GetAPI.utility.Utility;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
@Order(4)
public class RequestBodyModifyFilter
        extends OncePerRequestFilter {
	
	@Autowired
	TAuthApiMappingRepository tAuthApiMappingRepository;
	
	@Autowired
	GlobalContextBean globalContextBean;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
    	
    	try {
    		
    		ObjectMapper mapper = new ObjectMapper();
    		Map<String, Object> globalContext = new HashMap<String, Object>();
        	User authUser = Utility.getSecurityAuthentication();
        	
        	String requestUrl = request.getRequestURI();
        	String requestMethod = request.getMethod();
        	
        	globalContext.put("requestUrl", requestUrl);
        	globalContext.put("requestMethod", requestMethod);
        	globalContextBean.setAuthContextMap(globalContext);
        	
        	TAuthApiMapping tApiMapping = tAuthApiMappingRepository
        			.findByUsergroupIdAndApiEpAndApiMethod(11111L, requestUrl, requestMethod)
        			.orElse(null);
        	
        	if(authUser == null ){
        		filterChain.doFilter(request, response);
        		return;
        	}
        	
        	// System variable Injection
        	globalContext.put("userid", authUser.getUserId());
        	globalContext.put("usergroupid", authUser.getUsergroupId());
        	globalContext.put("username", authUser.getUsername());
        	globalContext.put("userrole", authUser.getUserRole());
        	globalContext.put("useremail", authUser.getUserEmail());  
        	
        	
            if( tApiMapping == null ) {
        		filterChain.doFilter(request, response);
        		return;
        	}
        	
        	Map<String,String> OWJSON =
                    mapper.readValue(tApiMapping.getOwJson(), Map.class);
        	
            String body =
                    request.getReader()
                           .lines()
                           .collect(Collectors.joining());
            

            Map<String,Object> bodyJSON = body.equals(null) || body.equals("") ? new HashMap<String, Object>()
            		: mapper.readValue(body, Map.class);
            
            Map<String, String[]> modifiedParams = new HashMap<String, String[]>();

            request.getParameterMap().forEach((key, value) ->
                modifiedParams.put(key, value.clone())
            );
            //bodyJSON.put("modifiedBy", "JWT_FILTER");

            
            for( Map.Entry<String, String> mpMap :  OWJSON.entrySet() ) {
            	
            	String[] keyset = mpMap.getKey().split("@");
            	String[] valueset = mpMap.getValue().split("@");
            	
            	String valueType = valueset[0];
            	String valueString = valueset[1];
            	
            	String keyType = keyset[0];
            	String keyString = keyset[1];
            	
            	if( keyType.equals("BODY") ) {
            		if("SEQ".equalsIgnoreCase(valueType)) {
            			String generatedSeqString =  this.tAuthApiMappingRepository
                				.getSequenceByCode(authUser.getUsergroupId(), valueString)
                				.orElse(null);
            			bodyJSON.put(keyString, generatedSeqString);
            		}
            		else if(globalContext.containsKey(valueString)) {
                		bodyJSON.put(keyString, globalContext.get(valueString));
            		}
            		else {
            			bodyJSON.put(keyString, valueString);
            		}
            	}
            	else if( keyType.equals("PARAM") ) {
            		if(globalContext.containsKey(valueString)) {
            			modifiedParams.put(keyString, new String[] { mapper.writeValueAsString(globalContext.get(valueString))});
            		}
            		else {
            			modifiedParams.put(keyString, new String[] {valueString});
            		}
            	}
            	
            }
            
            
            
            
            
            String modifiedBody =
                    mapper.writeValueAsString(bodyJSON);
            
            ModifyBodyHttpServletRequest wrapped =
                    new ModifyBodyHttpServletRequest(
                            request,
                            modifiedBody,
                            modifiedParams);	

            filterChain.doFilter(wrapped, response);
            
		} catch (Exception e) {
			filterChain.doFilter(request, response);
		}
        
    }
}