package ru.ffanjex.backenddevelopment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class MedicationScheduleRequest {

    public String medication_name;
    public List<Integer> days;

    @JsonFormat(pattern = "HH:mm")
    public List<LocalTime> times;

    public MedicationScheduleRequest(String medication_name, List<Integer> days, List<LocalTime> times) {
        this.medication_name = medication_name;
        this.days = days;
        this.times = times;
    }
}