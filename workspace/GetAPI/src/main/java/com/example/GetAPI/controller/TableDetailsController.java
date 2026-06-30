package com.example.GetAPI.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GetAPI.dao.TableName;
import com.example.GetAPI.dto.SearchParameter;
import com.example.GetAPI.repository.TableNameRepository;
import com.example.GetAPI.validation.TableDetailsValidation;

@RestController
@RequestMapping("/{table-name}")
public class TableDetailsController {
	
	@Autowired
	private TableDetailsValidation tableDetailsValidation;
	
	
//	private final static Long TABLE_USERID_LONG = 12345L;
	
	private final Logger log = LoggerFactory.getLogger(TableDetailsController.class);
	
	
	@GetMapping("/table-demo")
	public Object getTableColumn( @PathVariable("table-name") String tableName ) {
		
		try {
			
			log.debug("Entering into getTable Debug");
			log.info("Entering into getTable Info");
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
		
	}
	
	@GetMapping("/get-column")
	public ResponseEntity<Object> getColumn( @PathVariable("table-name") String tableName ) {
		
		try {
			
			log.debug("Entering into getTable " + tableName );
			
			Object dataObject = this.tableDetailsValidation.getColumn( tableName );
			return ResponseEntity.status(200).body(dataObject);
			
		} catch (Exception e) {
			log.debug("Exception : /save :- " + e);
			return ResponseEntity.status(404).body(Map.of("cause", e.getMessage()));
		}

	}
	
	
	
	
//	{table-name}/get											-- searching
//	{table-name}/get?product_name=price&stocks_count=4			-- filtering
//	{table-name}/get?product_name=price&OrderBy=product_name	-- sorting
//	{table-name}/get?product_name=price&PageNo=1&RowCount=10    -- pagination
	
	
	@GetMapping("/get")
	public ResponseEntity<Object> getTableData( @PathVariable("table-name") String tableName,
			@RequestParam MultiValueMap<String,String> queryParams ) {
		
		try {
			
			log.debug("Entering into getTable " + tableName );
			
			Object dataObject = tableDetailsValidation.getTableRow(tableName, queryParams, null);
	        
			System.out.println(queryParams);
			
			return ResponseEntity.status(200).body(dataObject);
			
		} catch (Exception e) {
			log.debug("Exception : /save :- " + e);
			return ResponseEntity.status(404).body(Map.of("cause", e.getMessage()));
		}

	}
	
	
	@PostMapping("/get")
	public ResponseEntity<Object> getTableDataByOperator( @PathVariable("table-name") String tableName,
			@RequestBody List<SearchParameter> lstSearchParameters ) {
		
		try {
			
			log.debug("Entering into getTableDataByOperator " + tableName );
			
			Object dataObject = tableDetailsValidation.getTableRowByOperator(tableName, lstSearchParameters);
	        
			System.out.println(lstSearchParameters);
			
			return ResponseEntity.status(200).body(dataObject);
			
		} catch (Exception e) {
			log.debug("Exception : /save :- " + e);
			return ResponseEntity.status(404).body(Map.of("cause", e.getMessage()));
		}

	}
	
	
	
	
	
	@GetMapping("/get/{primary-id}")
	public ResponseEntity<Object> getTableDataById( @PathVariable("table-name") String tableName,
			@PathVariable("primary-id") Long primaryId ) {
		
		try {
			
			log.debug("Entering into getTable " + tableName );
			
			Object dataObject = tableDetailsValidation.getTableDataById(tableName, primaryId);
			return ResponseEntity.status(200).body(dataObject);
			
		}
		catch (IllegalArgumentException e) {
			log.debug("IllegalArgumentException : /get/{primary-id} :- " + e);
			return ResponseEntity.status(400).body(Map.of("cause", e.getMessage()));
		}
		catch (Exception e) {
			log.debug("Exception : /save :- " + e);
			return ResponseEntity.status(404).body(Map.of("cause", e.getMessage()));
		}

	}
	
	
	
	
	
	
	
	
	@PostMapping("/save")
	public ResponseEntity<Object> saveTableData ( @PathVariable("table-name") String tableName,
			@RequestBody Map<String, Object> bodyParams ) {
		
		try {
			
			log.debug("Entering into saveTableData " + tableName );
			
			Object dataObject = tableDetailsValidation.saveTableData(tableName, bodyParams);
			
			return ResponseEntity.status(200).body(dataObject);
			
		} catch (Exception e) {
			log.debug("Exception : /save :- " + e);
			return ResponseEntity.status(404).body(Map.of("cause", e.getMessage()));
		}

	}
	
	
	// Updating data based on Id
	
	@PutMapping("/update/{primary-id}")
	public ResponseEntity<Object> updateTableData ( 
			@PathVariable("table-name") String tableName,
			@PathVariable("primary-id") Long primaryId, 
			@RequestBody Map<String, Object> pRowData  ){
		
		try {
			
			log.debug("Entering into updateTableData primaryId - " + primaryId );
			
			Object dataObject = tableDetailsValidation.updateRowById(tableName, primaryId, pRowData);
			
			return ResponseEntity.status(200).body(dataObject);
			
		} catch (Exception e) {
			log.debug("Exception : /save :- " + e);
			return ResponseEntity.status(404).body(Map.of("cause", e.getMessage()));
		}
		
	}
	
	

}
