package com.example.identity_final.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //khai bao enum, no la hang so, ma hang nay la 1 object co 2 thuoc tinh
    USER_EXIST(401,"user has been existed."),
    UNCATEGORIZED_EXCEPTION(999, "uncategorized exception");
    //define error code and message
    private int code;
    private String message;
}
