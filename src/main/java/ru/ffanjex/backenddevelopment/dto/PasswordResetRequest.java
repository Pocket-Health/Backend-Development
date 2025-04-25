package ru.ffanjex.backenddevelopment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordResetRequest {
    private String email;
    private String password;
}
