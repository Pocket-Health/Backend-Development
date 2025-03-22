package ru.ffanjex.backenddevelopment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ffanjex.backenddevelopment.entity.MedicalCard;

import java.util.UUID;

public interface MedicalCardRepository extends JpaRepository<MedicalCard, UUID> {
}
