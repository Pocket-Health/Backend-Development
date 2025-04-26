package ru.ffanjex.backenddevelopment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class MedicationScheduleRequest {

    public String medication_name;
    public List<Integer> days;

    @JsonFormat(pattern = "HH:mm")
    public List<LocalTime> times;
}