package com.example.identity_final.dto.response;

import lombok.Data;

//dto: can getter va setter de mapp data
@Data
public class ApiResponse <T>{
    //chuan hoa response  tra ve
    private int code;
    private String message;
    private T result;

}
