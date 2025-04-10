package ru.ffanjex.backenddevelopment.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.entity.MedicationSchedule;
import ru.ffanjex.backenddevelopment.service.MedicationScheduleService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/medical_schedule")
@AllArgsConstructor
public class MedicationScheduleController {

    private final MedicationScheduleService medicationScheduleService;

    @GetMapping("/schedules")
    public ResponseEntity<List<MedicationSchedule>> getMedicationSchedules(@RequestParam UUID userId) {
        List<MedicationSchedule> medicationSchedules = medicationScheduleService.getUserSchedules(userId);
        return new ResponseEntity<>(medicationSchedules, HttpStatus.OK);
    }

    @PostMapping("/add_schedule")
    public ResponseEntity<MedicationSchedule> addSchedule(@RequestBody MedicationSchedule medicationSchedule) {
        MedicationSchedule addedMedicationSchedule = medicationScheduleService.addMedicationSchedule(medicationSchedule);
        return new ResponseEntity<>(addedMedicationSchedule, HttpStatus.CREATED);
    }

    @PutMapping("/edit_schedule/{id}")
    public ResponseEntity<MedicationSchedule> editSchedule(@PathVariable UUID medicationScheduleId,
                                                           @RequestBody MedicationSchedule medicationSchedule) {
        Optional<MedicationSchedule> updatedMedicationSchedule = medicationScheduleService
                .editMedicationSchedule(medicationScheduleId, medicationSchedule);

        return new ResponseEntity<>(updatedMedicationSchedule.get(), HttpStatus.OK);
    }

    @DeleteMapping("/delete_schedule/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable UUID medicationScheduleId) {
        medicationScheduleService.deleteMedicationSchedule(medicationScheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}