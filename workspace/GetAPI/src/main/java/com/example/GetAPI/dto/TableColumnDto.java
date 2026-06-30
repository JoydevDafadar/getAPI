package com.example.GetAPI.dto;

import com.example.GetAPI.enums.Constraints;
import com.example.GetAPI.enums.Datatypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TableColumnDto {
	
	
	public static final Map<String, String> DTO_TO_SETTER_MAP = new HashMap<>();
	
	static {
        // Initialize DTO to Setter mappings
        DTO_TO_SETTER_MAP.put("getColumnName", "setTblColName");
        DTO_TO_SETTER_MAP.put("getColumnType", "setTblColType");
        DTO_TO_SETTER_MAP.put("getColumnLength", "setTblColLength");
        DTO_TO_SETTER_MAP.put("getDefaultValue", "setTblColDefault");
        DTO_TO_SETTER_MAP.put("getNullable", "setTblColNullable");
        DTO_TO_SETTER_MAP.put("getSequence", "setTnlColSeq");
        DTO_TO_SETTER_MAP.put("getMandatory", "setTblColNullable"); // Note: mandatory is inverted for nullable
        DTO_TO_SETTER_MAP.put("getConstraints", "setLstConstraints");
	}

    @JsonProperty("columnName")
    @NotBlank(message = "Column name is required")
    @Size(max = 100, message = "Column name cannot exceed 100 characters")
    private String columnName;

    @JsonProperty("columnType")
    @NotNull(message = "Column Type is required")
    private Datatypes columnType;

    @JsonProperty("columnLength")
    private Integer columnLength;

//    @JsonProperty("defaultValue")
//    private String defaultValue;

    @JsonProperty("mandatory")
    private Boolean mandatory;

    @JsonProperty("constraints")
    private List<Constraints> constraints;

//    @JsonProperty("sequence")
//    private Integer sequence;
    
}