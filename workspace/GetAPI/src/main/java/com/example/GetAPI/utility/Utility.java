package com.example.GetAPI.utility;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.sql.Update;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.GetAPI.dao.TableColumn;
import com.example.GetAPI.dao.TableRow;
import com.example.GetAPI.dao.User;
import com.example.GetAPI.enums.Datatypes;

import jakarta.persistence.Column;

public class Utility {
	
	public static User getSecurityAuthentication() {
		Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if( principle instanceof User  ) {
			return ((User) principle);
		}
		return null;
	}
	
	public static void callSetter(
	        Object target,
	        String setterName,
	        Object value
	) {
	    try {
	        Class<?> valueType = value.getClass();

	        Method method = target.getClass()
	                .getMethod(setterName, valueType);

	        method.invoke(target, value);

	    } catch (NoSuchMethodException e) {
	        throw new RuntimeException("Setter not found: " + setterName, e);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static Object callGetter(
	        Object target,
	        String getterName	) {
	    try {

	        Method method = target.getClass()
	                .getMethod(getterName);

	        return method.invoke(target);

	    } catch (NoSuchMethodException e) {
	        throw new RuntimeException("getterName not found: " + getterName, e);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static void updateObjectValue( Object target, Object value) {
		target = value;
	}
	
	
	public static Map<String, Object> TblRowToObjectDto( List<TableColumn> tableColumn, TableRow tableRow ){
		
		try {
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			
			tableColumn.forEach( ( eachColumn ) -> {

				String colName = eachColumn.getTblColName();
				
				StringBuilder tempString =  new StringBuilder("getCol");
				tempString.append( Utility.getColInd(eachColumn) );
				
				Object value = Utility.callGetter(tableRow, tempString.toString());
				returnMap.put(colName, value);
				
				
			});
			
			return returnMap;
			
		} catch (Exception e) {
	        throw new RuntimeException(e);
	    }
		
	}
	
	public static String getColInd( TableColumn tableColumn ){
		
		try {
			
			StringBuilder tempString =  new StringBuilder("");
			int sequence = tableColumn.getTblColSeq();
			
		
			Datatypes dt = tableColumn.getTblColType();
			tempString.append( sequence );
			tempString.append( dt.getCode() );
			
			return tempString.toString();
			
		} catch (Exception e) {
	        throw new RuntimeException(e);
	    }
		
	}
	
	public static String basicStringValidation( String inputString, String elementName ) throws IllegalArgumentException {
		
		if( inputString == null ) {
			throw new IllegalArgumentException( elementName + " can not be null." );
		}
		String str = inputString.trim();
		
		if( inputString.equals("") || str.length() <= 0 ) {
			throw new IllegalArgumentException( elementName + " can not be null." );
		}
		
		return str;
		
	}
	
	public static <T> T isNullOrEmpty( T inputString, String elementName ) throws IllegalArgumentException {
		
		if( inputString == null ) {
			throw new IllegalArgumentException( elementName + " can not be null." );
		}
		else if( inputString instanceof String ) {
			
			String str = ((String)inputString).trim();
			
			if( inputString.equals("") || str.length() <= 0 ) {
				throw new IllegalArgumentException( elementName + " can not be empty." );
			}
			
			return (T) str;
		}
		else if( inputString instanceof Long || inputString instanceof Long || inputString instanceof BigDecimal ) {
			
			return inputString;

		}
		else {
			throw new IllegalArgumentException( elementName + " can not be parse." );
		}
		
	}
	
	public static Object refreashType( Object input, Datatypes dt ) {
		try {
			
			switch (dt) {
			case  LNG : {
				
				if( input instanceof Long ) {
					return input;
				}
				else {
					return Long.parseLong(input.toString());
				}
			}
			default:
				return input;
			}
		} catch (Exception e) {
			return input;
		}
	}
	

}
