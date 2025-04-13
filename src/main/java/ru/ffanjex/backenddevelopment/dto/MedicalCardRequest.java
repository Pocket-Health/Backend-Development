package ru.ffanjex.backenddevelopment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicalCardRequest {
    private String fullname;
    private Integer height;
    private BigDecimal weight;
    private String blood_type;
    private String allergies;
    private String diseases;
}
