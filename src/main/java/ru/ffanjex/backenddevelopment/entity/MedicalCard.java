package ru.ffanjex.backenddevelopment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name = "medical_cards")
public class MedicalCard {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 255)
    @NotNull(message = "FullName can't be null")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight;

    @Size(max = 10)
    @NotNull(message = "BloodType can't be null")
    @Column(name = "blood_type", nullable = false, length = 10)
    private String bloodType;

    @Column(name = "allergies", length = Integer.MAX_VALUE)
    private String allergies;

    @Column(name = "diseases", length = Integer.MAX_VALUE)
    private String diseases;

}