package com.example.identity_final.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String token; // luu token tu server de tra ve cho user (FE)
    //true cc dung username va password
    boolean authentication;
}
