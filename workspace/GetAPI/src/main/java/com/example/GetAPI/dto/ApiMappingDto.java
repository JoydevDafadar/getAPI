package com.example.GetAPI.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ApiMappingDto {

    private String url;
    private String method;
    private String controllerClass;
    private String controllerMethod;
    private String requestDTO;
    private String responseDTO;
    
    
}