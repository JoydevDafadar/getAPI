package com.example.GetAPI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/{table-name}")
public class CoreTableController {
	
	private final Logger log = LoggerFactory.getLogger(CoreTableController.class);
	
	
	
	
//	"columnName" : "blog-name",
//    "columnType" : "STR",
//    "columnLength" : 50,
//    "mandatory" : true,
//    "constraints" : ["UNQ"]
	
	@GetMapping("/crate")
	public Object getTableColumn( @PathVariable("table-name") String tableName ) {
		
		try {
			
			log.debug("Entering into getTable Debug");
			log.info("Entering into getTable Info");
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
		
	}
	
	
	

}
