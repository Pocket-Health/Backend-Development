package ru.ffanjex.backenddevelopment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CodeVerificationRequest {
    private String email;
    private Integer code;
}
