package com.example.identity_final.Service;

import com.example.identity_final.Repository.UserRepository;
import com.example.identity_final.dto.request.AuthenticationRequest;
import com.example.identity_final.dto.response.AuthenticationResponse;
import com.example.identity_final.exception.AppException;
import com.example.identity_final.exception.ErrorCode;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.aspectj.weaver.ast.Var;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    //lay thong tin user
    UserRepository userRepository;
    //co dung cho cho khac nua
    @NonFinal // ko inject vao constructor
    protected static final String SIGNER_KEY = "v0yKrFkJyREX/RXFr4vr8exjiOawBMySpWNWKBQBZcIqNzpTR9GuMRss4CKNe4Cd";

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

    //tao method generate token: chi can thong tin username o thoi diem hien tai
    private String generateToken(String username) {
        //jwt co 3 phan: header, payload, sign
        //tao header: voi thuat toan hs512: de bam
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        //tao payload voi nhung thong tin can thiet
        //DATA TRONG  body dung claim
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("nguyetminh.com") //token dc issuer tu dai: tu domain
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() //het han trong 1 tieng
                )) //xac dinh tgian het han token
                .claim("customClaim","custom")
                .build();
        //payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); //convert payload ve json
        //jwt object can 2 param: header va payload
        JWSObject jwsObject = new JWSObject(header, payload);


        //ki token: voi khoa bi mat, co the dung khoa cong khai
        //cung cap khoa bi mat: 32byte 256bit: ban dau: len trang web gerenrate
        jwsObject.sign(new MACSigner(SIGNER_KEY));

    }
}
