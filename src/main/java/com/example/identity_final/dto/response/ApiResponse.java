package com.example.identity_final.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

//dto: can getter va setter de mapp data
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    //chuan hoa response  tra ve
    private int code = 200;
    private String message;
    private T result;

}
