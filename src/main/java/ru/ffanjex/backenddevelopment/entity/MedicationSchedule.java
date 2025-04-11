package ru.ffanjex.backenddevelopment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "medication_schedule")
public class MedicationSchedule {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Size(max = 255)
    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "scheduled_day", nullable = false)
    private List<Integer> scheduledDays;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "scheduled_time", nullable = false)
    private List<LocalTime> scheduledTimes;
}