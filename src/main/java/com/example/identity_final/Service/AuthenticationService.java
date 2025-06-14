package com.example.identity_final.Service;

import com.example.identity_final.Entity.User;
import com.example.identity_final.Repository.UserRepository;
import com.example.identity_final.dto.request.AuthenticationRequest;
import com.example.identity_final.dto.request.IntrospectRequest;
import com.example.identity_final.dto.response.AuthenticationResponse;
import com.example.identity_final.dto.response.IntrospectResponse;
import com.example.identity_final.exception.AppException;
import com.example.identity_final.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    //lay thong tin user
    UserRepository userRepository;
    //co dung cho cho khac nua
    @NonFinal // ko inject vao constructor
    @Value("${jwt.signerKey}") // doc bien tu file yaml
    protected String SIGNER_KEY;

    //xac thuc token
    public IntrospectResponse introspect(IntrospectRequest request)
        throws  JOSEException, ParseException {
        var token = request.getToken();

        //khởi tạo đối tượng dùng để xác minh.
        //Nó giống như việc bạn chuẩn bị một cây bút để ký, nhưng chưa ký gì cả.
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        //dùng verifier để kiểm tra chữ ký của token.
        SignedJWT signedJWT = SignedJWT.parse(token);

        //check xem token het han chua
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        //tra ve true false
        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date())) // tgian het han sau thoi diem hien tai
                .build();


    }
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

        //tao token neu dang nhap thanh cong
        var token = generateToken(request.getUsername());
        return AuthenticationResponse.builder()
                .token(token)
                .authentication(true)
                .build();

    }

    //tao method generate token: chi can thong tin username o thoi diem hien tai
    private String generateToken(User user) {
        //jwt co 3 phan: header, payload, sign
        //tao header: voi thuat toan hs512: de bam
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        //tao payload voi nhung thong tin can thiet
        //DATA TRONG  body dung claim
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("nguyetminh.com") //token dc issuer tu dai: tu domain
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() //het han trong 1 tieng
                )) //xac dinh tgian het han token
                //them role
                .claim("scope","custom")
                .build();
        //payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); //convert payload ve json
        //jwt object can 2 param: header va payload
        JWSObject jwsObject = new JWSObject(header, payload);


        //ki token: voi khoa bi mat, co the dung khoa cong khai
        //cung cap khoa bi mat: 32byte 256bit: ban dau: len trang web gerenrate
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            // Chuyển đối tượng JWSObject thành chuỗi JWT đã được ký (signed JWT string) và trả về chuỗi đó.
            return jwsObject.serialize();
            //Nếu khóa quá ngắn hoặc có lỗi khi ký → sẽ ném JOSEException.
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }

    }

    private String buildScope(User user){
        //scope la 1 list
        //giúp nối nhiều chuỗi lại với nhau, co the them dau phan cach, tien to, hau to
        StringJoiner stringJoiner = new StringJoiner(" "); //phan cac bang dau cach
        if (CollectionUtils.isEmpty(user.getRoles())){ //ktra null
            //Dòng này dùng để lặp qua tất cả các phần tử (role) trong
            // danh sách user.getRoles() và thêm từng role vào StringJoiner.
            user.getRoles().forEach(stringJoiner::add);
//            tuong duong voi:
//            for (String role : user.getRoles()) {
//                stringJoiner.add(role);
//            }
        }
// trả về một chuỗi (String) gồm các quyền (role) của người dùng user,
// được nối với nhau bằng dấu cách (" ").
        return stringJoiner.toString();
    }
}
