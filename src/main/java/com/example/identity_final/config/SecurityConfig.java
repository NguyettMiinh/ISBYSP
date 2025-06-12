package com.example.identity_final.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//noi cau hinh he thong
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/**").permitAll() // Cho phép POST /login không cần auth
                        .anyRequest().authenticated() // Các endpoint khác thì cần xác thực
                )
                .formLogin(form -> form.disable());
        return http.build();
    }

}
