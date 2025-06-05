package com.example.identity_final.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

//cung cap username va password de dang nhap
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    String username;
    String password;
}
