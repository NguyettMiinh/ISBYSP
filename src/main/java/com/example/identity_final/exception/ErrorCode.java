package com.example.identity_final.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //khai bao enum, no la hang so, ma hang nay la 1 object co 2 thuoc tinh
    USER_EXIST(401,HttpStatus.BAD_REQUEST,"user has been existed."),
    UNCATEGORIZED_EXCEPTION(999, HttpStatus.INTERNAL_SERVER_ERROR, "uncategorized exception"),
    USERNAME_INVALID(403,HttpStatus.BAD_REQUEST,"username must be at least 5 characters"),
    PASSWORD_INVALID(404,HttpStatus.BAD_REQUEST,"Password must be at least 8 characters"),
    INVALID_KEY(405, HttpStatus.BAD_REQUEST,"Invalid key."),
    USER_NOT_EXISTED(406,HttpStatus.NOT_FOUND,"user not existed"),
    UNAUTHENTICATED(407,HttpStatus.UNAUTHORIZED, "unauthenticated");
    //define error code and message
    private int code;
    private HttpStatusCode httpStatusCode;
    private String message;
}
