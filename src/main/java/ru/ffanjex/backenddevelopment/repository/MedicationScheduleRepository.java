package ru.ffanjex.backenddevelopment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ffanjex.backenddevelopment.entity.MedicationSchedule;
import ru.ffanjex.backenddevelopment.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, UUID> {
    List<MedicationSchedule> findByUser(User user);
    MedicationSchedule findByIdAndUser(UUID id, User user);
}