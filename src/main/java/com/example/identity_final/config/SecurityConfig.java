package com.example.identity_final.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

//noi cau hinh he thong
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //nhung endpoint can bao ve, nhung endpoint ko can bao ve ma cho qua luon
        //dung method de cofig nhung request
        httpSecurity.authorizeHttpRequests(request ->
                //cho phep truy cap ko can security
                request.requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login","/auth/introspect").permitAll()
                        //con lai phai xac thuc
                        .anyRequest().authenticated());
        //tat csrf
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        return httpSecurity.build();
    }

}
