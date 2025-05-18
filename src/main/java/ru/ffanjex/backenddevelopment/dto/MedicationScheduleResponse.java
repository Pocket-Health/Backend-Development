package ru.ffanjex.backenddevelopment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationScheduleResponse {
    private UUID id;
    private String medicationName;
    private List<Integer> schedulesDays;
    private List<LocalTime> schedulesTime;
}
