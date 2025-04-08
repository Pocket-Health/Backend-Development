package ru.ffanjex.backenddevelopment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "medication_schedule")
public class MedicationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 255)
    @NotNull
    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @NotNull
    @ElementCollection
    @CollectionTable(name = "scheduled_days", joinColumns = @JoinColumn(name = "medication_schedule_id"))
    @Column(name = "scheduled_day")
    private List<Integer> scheduledDays;

    @NotNull
    @ElementCollection
    @CollectionTable(name = "scheduled_times", joinColumns = @JoinColumn(name = "medication_schedule_id"))
    @Column(name = "scheduled_time")
    private List<LocalTime> scheduledTimes;
}