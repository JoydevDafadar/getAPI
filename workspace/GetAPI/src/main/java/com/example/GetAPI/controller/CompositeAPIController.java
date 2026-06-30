package com.example.GetAPI.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.GetAPI.dto.CompositeRequestDto;
import com.example.GetAPI.validation.CompositeAPIValidation;


@RestController
public class CompositeAPIController {
	
	@Autowired
	private CompositeAPIValidation compositeAPIValidation;	
	
	private final Logger log = LoggerFactory.getLogger(CompositeAPIController.class);

	
	@PostMapping("/composite/compositeapi")
	public ResponseEntity<Object> compositeApi(
			@RequestBody CompositeRequestDto compositeRequestDto ) {
		
		try {
			
			log.debug("Entering into compositeApi >> " + compositeRequestDto.getLstExecutionDto().size() );
			
			Object dataObject = compositeAPIValidation.compositeApi(compositeRequestDto);
			return ResponseEntity.status(200).body(dataObject);
			
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
	
	

}
