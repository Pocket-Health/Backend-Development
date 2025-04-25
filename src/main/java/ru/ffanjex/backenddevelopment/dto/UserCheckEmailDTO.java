package ru.ffanjex.backenddevelopment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UserCheckEmailDTO {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
}
