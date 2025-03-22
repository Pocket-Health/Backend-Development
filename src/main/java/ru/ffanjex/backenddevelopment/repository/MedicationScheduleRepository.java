package ru.ffanjex.backenddevelopment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ffanjex.backenddevelopment.entity.MedicationSchedule;

import java.util.UUID;

public interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, UUID> {
}
