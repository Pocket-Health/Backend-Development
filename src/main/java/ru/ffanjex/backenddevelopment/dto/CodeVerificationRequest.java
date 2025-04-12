package ru.ffanjex.backenddevelopment.dto;

import lombok.Data;

@Data
public class CodeVerificationRequest {
    private String email;
    private Integer code;
}
