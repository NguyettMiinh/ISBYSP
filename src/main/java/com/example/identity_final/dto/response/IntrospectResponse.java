package com.example.identity_final.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectResponse {
    boolean valid;
}

//tai sao can phai xac thuc token co dung khong: vi chung ta se lay no lam input cho lan dang nhap toi nen se phai xac nhan
//lai phai khong
