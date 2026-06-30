package com.example.GetAPI.dto;

import com.example.GetAPI.enums.APIMethod;
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
public class ExecutionDto {
	
	@JsonProperty("IDENTITY")
    private String identity;

    @JsonProperty("URL")
    private String url;

    @JsonProperty("METHOD")
    private APIMethod method;

    @JsonProperty("PAYLOAD")
    private Object payload;

}
