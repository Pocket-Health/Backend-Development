package ru.ffanjex.backenddevelopment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Table(name = "knowledge_base")
public class KnowledgeBase {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 255)
    @NotNull(message = "Disease can't be null")
    @Column(name = "disease_name", nullable = false)
    private String diseaseName;

    @NotNull(message = "Description can't be null")
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @NotNull(message = "Types can't be null")
    @Column(name = "types", nullable = false, length = Integer.MAX_VALUE)
    private String types;

    @NotNull(message = "Symptoms can't be null")
    @Column(name = "symptoms", nullable = false, length = Integer.MAX_VALUE)
    private String symptoms;

    @NotNull(message = "Causes can't be null")
    @Column(name = "causes", nullable = false, length = Integer.MAX_VALUE)
    private String causes;

}