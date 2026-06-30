package com.example.GetAPI.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.example.GetAPI.dao.User;

public class CoreRefactorUtil {
	
//	private Object refactorIncomingObject( User user, Object target ) {
//			
//			
//			try {
//				
//				if (target == null) {
//		            return target;
//		        }
//	
//		        if (target instanceof List<?>) {
//	
//		            List<?> list = (List<?>) target;
//	
//		            ArrayList<Object> returnedList = new ArrayList<Object>();
//		            for (Object item : list) {
//		                Object returnedItem = refactorObject(globalContext, item);
//		                returnedList.add(returnedItem);
//		            }
//		            target = returnedList;
//		        }
//		        
//		        else if (target instanceof Map<?, ?>) {
//	
//		            Map<?, ?> map = (Map<?, ?>) target;
//		            
//		            Map<Object, Object> returnedMap = new HashMap<Object, Object>();
//		            
//		            for (Map.Entry<?, ?> entry : map.entrySet()) {
//	
//		                Object key = entry.getKey();
//		                Object value = entry.getValue();
//	
//		                System.out.println("Key = " + key);
//	
//		                Object returnedItem = refactorObject(globalContext, value);
//		                returnedMap.put(key, returnedItem);
//		            }
//		            target = returnedMap;
//		        }
//		        
//		        else if (target instanceof String) {
//	
//		            String value = (String) target;
//	
//		            System.out.println("String = " + value);
//	
//		            // Process placeholder replacement here
//		            Object replacedObject = this.replacePlaceHolder(value, globalContext);
//		            
//		            Object returnedItem = replacedObject;
//		            target = returnedItem;
//		        }
//	//	        else if (target instanceof Integer) {
//	//
//	//	            Integer value = (Integer) target;
//	//
//	//	            System.out.println("Integer = " + value);
//	//	        }
//	//	        else if (target instanceof Long) {
//	//
//	//	            Long value = (Long) target;
//	//
//	//	            System.out.println("Long = " + value);
//	//	        }
//	//	        else if (target instanceof Boolean) {
//	//
//	//	            Boolean value = (Boolean) target;
//	//
//	//	            System.out.println("Boolean = " + value);
//	//	        }
//		        else {
//	
//		            System.out.println("Unknown Type = " + target.getClass());
//		        }
//				
//		        return target;
//				
//			} catch (IllegalArgumentException e) {
//				throw new IllegalArgumentException(e.getMessage());
//			} catch (Exception e) {
//				throw new NoSuchElementException(e.getMessage());
//			}
//			
//			
//		}

}
