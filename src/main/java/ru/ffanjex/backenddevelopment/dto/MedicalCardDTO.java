package ru.ffanjex.backenddevelopment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicalCardDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;
    private Integer height;
    private BigDecimal weight;
    private String bloodType;
    private String allergies;
    private String diseases;
}
