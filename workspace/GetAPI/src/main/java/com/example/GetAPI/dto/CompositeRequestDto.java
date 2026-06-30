package com.example.GetAPI.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CompositeRequestDto {
	
	@JsonProperty("EXECUTION")
	private List<ExecutionDto> lstExecutionDto;
	
	@JsonProperty("OUTPUT")
	private Map<String, Object> output;
	
	
}
