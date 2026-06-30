package com.example.GetAPI.dto;

import com.example.GetAPI.enums.DBOperator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SearchParameter {
	
    @JsonProperty("parameterName")
    @NotBlank(message = "parameterName is required")
    @Size(max = 100, message = "parameterValue cannot exceed 100 characters")
    private String parameterName;

    @JsonProperty("parameterValue")
    @NotNull(message = "parameterValue is required")
    private String parameterValue;

    @JsonProperty("operator")
    @Builder.Default
    private DBOperator operator = DBOperator.EQL;

    
}