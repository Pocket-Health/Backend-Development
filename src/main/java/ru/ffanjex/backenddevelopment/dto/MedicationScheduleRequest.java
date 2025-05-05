package ru.ffanjex.backenddevelopment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "The name of the medicine", example = "Aspirin")
    public String medication_name;

    @Schema(description = "Reception days (1 = Mon, 7 = Sun)", example = "[1, 3, 5]")
    public List<Integer> days;

    @ArraySchema(
            schema = @Schema(
                    type = "string",
                    format = "HH:mm",
                    example = "08:30",
                    description = "Reception time (in format HH:mm)"
            )
    )
    @JsonFormat(pattern = "HH:mm")
    public List<LocalTime> times;
}