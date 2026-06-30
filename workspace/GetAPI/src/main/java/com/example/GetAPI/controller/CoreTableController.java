package com.example.GetAPI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GetAPI.dto.TableColumnDto;
import com.example.GetAPI.enums.ActionMethod;
import com.example.GetAPI.validation.CoreTableValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/{table-name}")
public class CoreTableController {
	
	private final Logger log = LoggerFactory.getLogger(CoreTableController.class);
	
	@Autowired
	CoreTableValidation coreTableValidation;
	
	
	
	
//	"columnName" : "blog-name",
//    "columnType" : "STR",
//    "columnLength" : 50,
//    "mandatory" : true,
//    "constraints" : ["UNQ"]
	
	@PostMapping("/create")
	public ResponseEntity<Object> crateTable( 
			@PathVariable("table-name") String tableName,
			@Valid @RequestBody List<TableColumnDto> lstTableColumnDto) {
		
		try {
			
			log.debug("Entering into crateTable Debug tableName - " + tableName );
			Object object = this.coreTableValidation.createOrUpdateTable(tableName, lstTableColumnDto, ActionMethod.CREATE);
			
			return ResponseEntity.status(200).body(object);
			
		} catch (IllegalArgumentException e) {
			log.debug("IllegalArgumentException : /get/{primary-id} :- " + e);
			return ResponseEntity.status(400).body(Map.of("cause", e.getMessage()));
		} catch (Exception e) {
			log.debug("Exception : /save :- " + e);
			return ResponseEntity.status(404).body(Map.of("cause", e.getMessage()));
		}
		
	}
	
	@PostMapping("/alter")
	public ResponseEntity<Object> alterTable( 
			@PathVariable("table-name") String tableName,
			@Valid @RequestBody List<TableColumnDto> lstTableColumnDto) {
		
		try {
			
			log.debug("Entering into crateTable Debug tableName - " + tableName );
			Object object = this.coreTableValidation.createOrUpdateTable(tableName, lstTableColumnDto, ActionMethod.ALTER);
			
			return ResponseEntity.status(200).body(object);
			
		} catch (IllegalArgumentException e) {
			log.debug("IllegalArgumentException : /get/{primary-id} :- " + e);
			return ResponseEntity.status(400).body(Map.of("cause", e.getMessage()));
		} catch (Exception e) {
			log.debug("Exception : /save :- " + e);
			return ResponseEntity.status(404).body(Map.of("cause", e.getMessage()));
		}
		
	}
	
	
	

}
