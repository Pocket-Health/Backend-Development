package ru.ffanjex.backenddevelopment.dto;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;
}
