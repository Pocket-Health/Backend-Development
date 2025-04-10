package ru.ffanjex.backenddevelopment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MedicalCardDTO {
    private String email;
    private String fullName;
    private Integer height;
    private BigDecimal weight;
    private String bloodType;
    private String allergies;
    private String diseases;
}
