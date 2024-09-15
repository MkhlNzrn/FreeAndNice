package com.example.freeandnice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordRequest {
    private String email;
    private Long pin;
    private String password;
}
