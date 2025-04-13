package ru.ffanjex.backenddevelopment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ffanjex.backenddevelopment.entity.MedicalCard;
import ru.ffanjex.backenddevelopment.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicalCardRepository extends JpaRepository<MedicalCard, UUID> {
    Optional<MedicalCard> findByUser(User user);
}
