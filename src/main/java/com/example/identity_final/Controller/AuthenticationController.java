package com.example.identity_final.Controller;

import com.example.identity_final.Service.AuthenticationService;
import com.example.identity_final.dto.request.AuthenticationRequest;
import com.example.identity_final.dto.request.IntrospectRequest;
import com.example.identity_final.dto.response.ApiResponse;
import com.example.identity_final.dto.response.AuthenticationResponse;
import com.example.identity_final.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//api dung cho viec xac thuc, thuong la de dang nhap, dang ki, xac thuc gi do, change password
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    //xac thuc token
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
        throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
