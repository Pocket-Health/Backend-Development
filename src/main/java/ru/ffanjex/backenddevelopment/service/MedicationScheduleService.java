package ru.ffanjex.backenddevelopment.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ffanjex.backenddevelopment.entity.MedicationSchedule;
import ru.ffanjex.backenddevelopment.repository.MedicationScheduleRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MedicationScheduleService {

    private final MedicationScheduleRepository medicationScheduleRepository;

    public List<MedicationSchedule> getUserSchedules(UUID userId) {
        return medicationScheduleRepository.findByUserId(userId);
    }

    public MedicationSchedule addMedicationSchedule(MedicationSchedule medicationSchedule) {
        return medicationScheduleRepository.save(medicationSchedule);
    }

    public Optional<MedicationSchedule> editMedicationSchedule(UUID medicationScheduleId,
                                                               MedicationSchedule newMedicationSchedule) {
        return medicationScheduleRepository.findById(medicationScheduleId)
                .map(schedule -> {
                    schedule.setMedicationName(newMedicationSchedule.getMedicationName());
                    schedule.setScheduledDays(newMedicationSchedule.getScheduledDays());
                    schedule.setScheduledTimes(newMedicationSchedule.getScheduledTimes());
                    return medicationScheduleRepository.save(schedule);
                });
    }

    public void deleteMedicationSchedule(UUID id) {
        medicationScheduleRepository.deleteById(id);
    }
}