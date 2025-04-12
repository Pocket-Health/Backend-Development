package ru.ffanjex.backenddevelopment.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
    private String password;
}
