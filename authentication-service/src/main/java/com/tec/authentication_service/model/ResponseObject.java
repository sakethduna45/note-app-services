package com.tec.authentication_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseObject {
    private String accessToken;
    private String refreshToken;
    private String message;
}
