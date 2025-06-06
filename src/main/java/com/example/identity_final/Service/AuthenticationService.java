package com.example.identity_final.Service;

import com.example.identity_final.Repository.UserRepository;
import com.example.identity_final.dto.request.AuthenticationRequest;
import com.example.identity_final.dto.response.AuthenticationResponse;
import com.example.identity_final.exception.AppException;
import com.example.identity_final.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.weaver.ast.Var;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    //lay thong tin user
    UserRepository userRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        //lay thong tin user
        // bien var la bien noi suy: tuc no se tu noi suy kieu tra ve
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        // kiem tra mat khau trong db voi luc nhap co khuc khong
        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                user.getPassword());

        //neu dang nhap loi thi tra ve loi
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }


    }
}
