package ru.ffanjex.backenddevelopment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Table(name = "medication_schedule")
public class MedicationSchedule {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

}