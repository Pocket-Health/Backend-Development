package ru.ffanjex.backenddevelopment.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.MedicationScheduleRequest;
import ru.ffanjex.backenddevelopment.entity.MedicationSchedule;
import ru.ffanjex.backenddevelopment.service.MedicationScheduleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/medical_schedule")
@RequiredArgsConstructor
@Validated
public class MedicationScheduleController {

    private final MedicationScheduleService medicationScheduleService;
    private static final Logger logger = LoggerFactory.getLogger(MedicationScheduleController.class);

    @GetMapping("/schedules")
    public ResponseEntity<List<MedicationSchedule>> getUserSchedules() {
        List<MedicationSchedule> schedules = medicationScheduleService.getUserSchedules();
        logger.info("Fetched {} schedules for user", schedules.size());
        return ResponseEntity.ok(schedules);
    }

    @PostMapping("/add_schedule")
    public ResponseEntity<MedicationSchedule> addSchedule(@RequestBody @Validated MedicationScheduleRequest request) {
        MedicationSchedule schedule = medicationScheduleService.addSchedule(request);
        logger.info("Created new schedule: {}", schedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
    }

    @PutMapping("/edit_schedule/{id}")
    public ResponseEntity<MedicationSchedule> editSchedule(@PathVariable UUID id,
                                                           @Validated @RequestBody MedicationScheduleRequest request) {
        MedicationSchedule updatedSchedule = medicationScheduleService.editSchedule(id, request);
        if (updatedSchedule != null) {
            return ResponseEntity.ok(updatedSchedule);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete_schedule/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable UUID id) {
        medicationScheduleService.deleteSchedule(id);
        logger.info("Deleted schedule with ID {}", id);
        return ResponseEntity.noContent().build();
    }
}