package ru.ffanjex.backenddevelopment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.MedicationScheduleRequest;
import ru.ffanjex.backenddevelopment.dto.MedicationScheduleResponse;
import ru.ffanjex.backenddevelopment.entity.MedicationSchedule;
import ru.ffanjex.backenddevelopment.service.MedicationScheduleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/medical_schedule")
@RequiredArgsConstructor
@Validated
@Tag(name = "Medication Schedule", description = "Endpoints for managing medication schedules")
public class MedicationScheduleController {

    private final MedicationScheduleService medicationScheduleService;
    private static final Logger logger = LoggerFactory.getLogger(MedicationScheduleController.class);

    @Operation(summary = "Get all user medication schedules", description = "Retrieves all medication schedules for the current user")
    @ApiResponse(responseCode = "200", description = "List of medication schedules",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MedicationSchedule.class)))
    @GetMapping("/schedules")
    public ResponseEntity<List<MedicationScheduleResponse>> getUserSchedules() {
        List<MedicationScheduleResponse> schedules = medicationScheduleService.getUserScheduleResponses();
        logger.info("Fetched {} schedules for user", schedules.size());
        return ResponseEntity.ok(schedules);
    }

    @Operation(summary = "Add new medication schedule", description = "Creates a new medication schedule for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medication schedule created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicationSchedule.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/add_schedule")
    public ResponseEntity<MedicationSchedule> addSchedule(@RequestBody @Validated MedicationScheduleRequest request) {
        MedicationSchedule schedule = medicationScheduleService.addSchedule(request);
        logger.info("Created new schedule: {}", schedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
    }

    @Operation(summary = "Edit existing medication schedule", description = "Updates the medication schedule by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medication schedule updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicationSchedule.class))),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @Content)
    })
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

    @Operation(summary = "Delete medication schedule", description = "Deletes the schedule by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Schedule successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @Content)
    })
    @DeleteMapping("/delete_schedule/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable UUID id) {
        medicationScheduleService.deleteSchedule(id);
        logger.info("Deleted schedule with ID {}", id);
        return ResponseEntity.noContent().build();
    }
}