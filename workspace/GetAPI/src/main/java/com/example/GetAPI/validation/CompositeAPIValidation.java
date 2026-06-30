package com.example.GetAPI.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;
import org.springframework.web.util.pattern.PathPatternParser;

import com.example.GetAPI.dto.ApiMappingDto;
import com.example.GetAPI.dto.CompositeRequestDto;
import com.example.GetAPI.dto.ExecutionDto;
import com.example.GetAPI.enums.APIMethod;
import com.example.GetAPI.utility.Constants;

import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

@Service
@ConfigurationProperties( prefix = "api" )
public class CompositeAPIValidation {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ConversionService conversionService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	Logger log = LoggerFactory.getLogger(CompositeAPIValidation.class);
	
	
	private List<ApiMappingDto> mappings;
	
	public List<ApiMappingDto> getMappings() {
		return mappings;
	}

	public void setMappings(List<ApiMappingDto> mappings) {
		this.mappings = mappings;
	}


	public Object compositeApi(
			CompositeRequestDto compositeRequestDto ) {
		
		try {
			Map<String, Object> gloabalContext = new HashMap<String, Object>();
			
			log.debug("Entering into compositeApi >> compositeRequestDto " );
			
			gloabalContext = this.itterateCoreApi( compositeRequestDto.getLstExecutionDto() );
			
			Object dataObject = this.refactorObject(gloabalContext, compositeRequestDto.getOutput());
			
			return dataObject;
			
		}
		catch (IllegalArgumentException e) {
			log.debug("IllegalArgumentException : compositeApi :- " + e);
			return ResponseEntity.status(400).body(Map.of("cause", e.getMessage()));
		}
		catch (Exception e) {
			log.debug("Exception : /compositeApi :- " + e);
			return ResponseEntity.status(404).body(Map.of("cause", e.getMessage()));
		}

	}
	
	
	public Map<String, Object> itterateCoreApi( List<ExecutionDto> lstExecutinDto ) {
		
		try {
			
			Map<String, Object> gloabalContext = new HashMap<String, Object>();
			
			
			lstExecutinDto.forEach( executionDto -> {
				
				Map<String, String> pathVarible = new HashMap<String, String>();
				MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
				Object requestBody;
				ApiMappingDto apiMapping;
				
				String url = executionDto.getUrl();
				
				// Replacing url templateVariable
				Object urlObject = this.replacePlaceHolder(url, gloabalContext);
				if( !(urlObject instanceof String) ) {
					throw new IllegalArgumentException(" URL parsing failed - " + url );
				}
				url = (String)urlObject;
				
				
				APIMethod method = executionDto.getMethod();
				String[] urlArray = url.split("\\?");
				
				String endpointString = urlArray[0];
				
				try {

					// Searching for best matched Endpoint including path variable.
					PathPatternParser parser = new PathPatternParser();
					PathContainer path =
				            PathContainer.parsePath(endpointString);
					
					apiMapping = mappings.stream()
							.filter( e -> {
								
							    PathPattern pattern = parser.parse(e.getUrl());
							    
							    boolean matched =
							            pattern.matches(path);
							    
								return ( matched && method == APIMethod.valueOf(e.getMethod()) );
							})
							.findFirst()
							.orElseThrow();
					
					PathPattern pattern = parser.parse(apiMapping.getUrl());
					
					// paring Path variable 
					PathMatchInfo info =
					        pattern.matchAndExtract(path);
					if (info != null) {
						pathVarible = info.getUriVariables();
					}
					
				} catch (Exception e) {
					throw new IllegalArgumentException("No such configuration found in the system for API - " + url );
				}
					
				
				// Paring query parameters
				try {
					
					UriComponents components = UriComponentsBuilder
							.fromUriString(url)
							.build();
					
					queryParams = components.getQueryParams();
					
				} catch (Exception e) {
					throw new IllegalArgumentException("Illegal Param Argument passes through the API - " + url );
				}
				
				requestBody = executionDto.getPayload();
				
//				String queryParamString = ( urlArray.length > 1 )? urlArray[1] : null;
//				if( queryParamString != null && !queryParamString.isEmpty() ) {
//					
//					for (String eachParam : queryParamString.split("&") ) {
//						
//						String[] paramStr = eachParam.split("=", 2);
//						
//						if( paramStr.length == 2 ) {
//							queryParams.put(paramStr[0], paramStr[1]);
//						}
//						else {
//							throw new IllegalArgumentException("Illegal Argument passes through the API - " 
//																						+ url + " param - " + eachParam );
//						}	
//					}
//				}
				
				
				
				String toControllerString = apiMapping.getControllerClass();
				String toMethodString = apiMapping.getControllerMethod();
				
			    Object controllerBean = applicationContext.getBean(toControllerString);
				
			    Method controllerMethod = Arrays.stream(controllerBean.getClass().getMethods())
			            .filter(m -> m.getName().equals(toMethodString))
			            .findFirst()
			            .orElseThrow();
			    
			    Object[] args = new Object[controllerMethod.getParameterCount()];
			    
			    
			    // Sequentially iterating
			    Parameter[] parameters = controllerMethod.getParameters();
			    
			    for (int i = 0; i < parameters.length; i++) {
			    	Parameter parameter = parameters[i];
			    	
			    	try {
			    		
			    		Class<?> targetType = parameter.getType();
			    		Type genericType = parameter.getParameterizedType();
			    		
				    	for ( Annotation annotation: parameter.getAnnotations()) {
				    		if(annotation instanceof RequestParam ) {
				    			
				    			if(MultiValueMap.class.isAssignableFrom(targetType)) {
				    				args[i] = queryParams;
				    			}
				    			else {
				    				
				    				 RequestParam rp =
				    		                    parameter.getAnnotation(RequestParam.class);
				    				 String name = rp.value();
				    				 String value = queryParams.getFirst(name);
				    				 
				    				args[i] = conversionService.convert(value, targetType);
				    				
				    			}
				    			
				    		}
				    		else if (annotation instanceof PathVariable) {
				    			
				    			PathVariable pv = parameter.getAnnotation(PathVariable.class);
				    	        String name = pv.value();
				    	        
				    	        args[i] =  conversionService.convert(pathVarible.get(name),targetType);
				    		}
				    		else if (annotation instanceof RequestBody ) {
				    			
				    			JavaType javaType = objectMapper.getTypeFactory()
				    					.constructType(genericType);
				    			
				    			Object obj = objectMapper.convertValue(requestBody, javaType);
				    			args[i] = obj;
				    		}
				    		
				    	}
						
					} catch (Exception e) {
						// TODO: handle exception
						throw new IllegalArgumentException("Illegal Param Argument passes through the API - " + url );
					}
			    	
			    }
			    
			    
			    // Calling method after constructing arguments
			    try {

					Object object = controllerMethod.invoke(controllerBean, args);
					
					gloabalContext.put(executionDto.getIdentity(), ((ResponseEntity)object).getBody() );
					
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					throw new IllegalArgumentException(e.getMessage());
				}
			    
			    
			    
	     }); 
			
			return gloabalContext;
			
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
		
	}
	
	
	
	
	private Object refactorObject( Object globalContext, Object target ) {
		
		
		try {
			
			if (target == null) {
	            return target;
	        }

	        if (target instanceof List<?>) {

	            List<?> list = (List<?>) target;

	            ArrayList<Object> returnedList = new ArrayList<Object>();
	            for (Object item : list) {
	                Object returnedItem = refactorObject(globalContext, item);
	                returnedList.add(returnedItem);
	            }
	            target = returnedList;
	        }
	        
	        else if (target instanceof Map<?, ?>) {

	            Map<?, ?> map = (Map<?, ?>) target;
	            
	            Map<Object, Object> returnedMap = new HashMap<Object, Object>();
	            
	            for (Map.Entry<?, ?> entry : map.entrySet()) {

	                Object key = entry.getKey();
	                Object value = entry.getValue();

	                System.out.println("Key = " + key);

	                Object returnedItem = refactorObject(globalContext, value);
	                returnedMap.put(key, returnedItem);
	            }
	            target = returnedMap;
	        }
	        
	        else if (target instanceof String) {

	            String value = (String) target;

	            System.out.println("String = " + value);

	            // Process placeholder replacement here
	            Object replacedObject = this.replacePlaceHolder(value, globalContext);
	            
	            Object returnedItem = replacedObject;
	            target = returnedItem;
	        }
//	        else if (target instanceof Integer) {
//
//	            Integer value = (Integer) target;
//
//	            System.out.println("Integer = " + value);
//	        }
//	        else if (target instanceof Long) {
//
//	            Long value = (Long) target;
//
//	            System.out.println("Long = " + value);
//	        }
//	        else if (target instanceof Boolean) {
//
//	            Boolean value = (Boolean) target;
//
//	            System.out.println("Boolean = " + value);
//	        }
	        else {

	            System.out.println("Unknown Type = " + target.getClass());
	        }
			
	        return target;
			
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
		
		
	}
	
	
	
	
	private Object replacePlaceHolder( String input, Object globalContext ) {
		
		try {
			
			Object responseObject = input;
			String trimedInput = input.trim();
			
			String expression = null;
			Boolean isPartialTemplate = false;
			
			// Checking Complete template or partial template
			if( trimedInput.startsWith(Constants.PLACEHOLDER_ST_SYMBOL) 
					&& trimedInput.endsWith(Constants.PLACEHOLDER_END_SYMBOL) ) {
				
				expression = trimedInput.substring(Constants.PLACEHOLDER_ST_SYMBOL.length(), 
						trimedInput.length() - Constants.PLACEHOLDER_END_SYMBOL.length());
				
			} else if ( trimedInput.contains(Constants.PLACEHOLDER_ST_SYMBOL) ) {
				
				isPartialTemplate = true;
				
				Pattern pattern = Pattern.compile(Constants.PLACEHOLDER_REGEX);
				Matcher matcher = pattern.matcher(input);

				if (matcher.find()) {
				    expression = matcher.group(1);
				}
								
			}
				
			if ( expression != null ) {
				
				String[] propertyLst = expression.split("\\.");
				
				Object contextScope = globalContext;
				
				for ( int i = 0; i < propertyLst.length; i++ ) {
					
					String eachExp = propertyLst[i];
					
					// BASE CASE
					if( i == (propertyLst.length-1) ) {
							
						if(eachExp.equalsIgnoreCase("ALL")) {
							responseObject = contextScope;
							break;
						}
						
					}
					
					try {
						
						if( eachExp.startsWith("[") && eachExp.endsWith("]") ) {
							
							Integer Arrind = Integer.valueOf( eachExp.substring(1, eachExp.length() - 1) );	
							if( contextScope instanceof List<?> ) {
								contextScope = ((List<?>)contextScope).get(Arrind);
							}
							else {
								throw new IllegalArgumentException(eachExp + " can not be solved cause of invalid DataType Conversion");
							}
							
						}
						else if ( contextScope instanceof Map ) {
							
							if( ((Map<String, ?>)contextScope).containsKey(eachExp) ) {		
								if( i == (propertyLst.length-1) ) {
									responseObject = ((Map<String, ?>)contextScope).get(eachExp);	
								}else {
									contextScope = ((Map<String, ?>)contextScope).get(eachExp);
								}	
							}
							else {
								return trimedInput;
							}
							
						}
						
					} catch (Exception e) {
						throw new IllegalArgumentException(eachExp + " can not be solved cause of - " + e.getMessage() );

					}
					
				}

				
			}
			
			// Base Case
			if ( isPartialTemplate ) {
				
				if ( responseObject instanceof String || responseObject instanceof Integer || responseObject instanceof Long ) {
					trimedInput = trimedInput.replaceFirst(Constants.PLACEHOLDER_REGEX, String.valueOf(responseObject));
					return this.replacePlaceHolder(trimedInput, globalContext);
				}
				throw new IllegalArgumentException("Template varible parsing error for " + expression );
			}
			
			return responseObject;
			
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
		
	}
	
	
}
