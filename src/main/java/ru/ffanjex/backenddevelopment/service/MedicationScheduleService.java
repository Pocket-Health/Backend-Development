package ru.ffanjex.backenddevelopment.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ffanjex.backenddevelopment.dto.MedicationScheduleResponse;
import ru.ffanjex.backenddevelopment.entity.MedicationSchedule;
import ru.ffanjex.backenddevelopment.dto.MedicationScheduleRequest;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.MedicationScheduleRepository;
import ru.ffanjex.backenddevelopment.repository.UserRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationScheduleService {

    private final MedicationScheduleRepository medicationScheduleRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(MedicationScheduleService.class);

    public List<MedicationScheduleResponse> getUserScheduleResponses() {
        String currentUserEmail = getCurrentUserEmail();
        User user = findUserByEmail(currentUserEmail);

        List<MedicationSchedule> schedules = medicationScheduleRepository.findByUser(user);

        return schedules.stream()
                .map(schedule -> new MedicationScheduleResponse(
                        schedule.getId(),
                        schedule.getMedicationName(),
                        schedule.getScheduledDays(),
                        schedule.getScheduledTimes()
                ))
                .toList();
    }

    public MedicationSchedule addSchedule(MedicationScheduleRequest request) {
        String currentUserEmail = getCurrentUserEmail();
        User user = findUserByEmail(currentUserEmail);

        validateRequest(request);

        MedicationSchedule schedule = new MedicationSchedule();
        schedule.setMedicationName(request.getMedication_name());

        List<Integer> scheduledDays = request.getDays();
        schedule.setScheduledDays(scheduledDays);

        List<LocalTime> scheduledTimes = request.getTimes();
        schedule.setScheduledTimes(scheduledTimes);

        schedule.setUser(user);

        return medicationScheduleRepository.save(schedule);
    }

    public MedicationSchedule editSchedule(UUID id, MedicationScheduleRequest newRequest) {
        String currentUserEmail = getCurrentUserEmail();
        User user = findUserByEmail(currentUserEmail);

        validateRequest(newRequest);

        Optional<MedicationSchedule> scheduleOpt = medicationScheduleRepository.findById(id);
        if (scheduleOpt.isEmpty()) {
            throw new IllegalArgumentException("Schedule not found");
        }

        MedicationSchedule medicationSchedule = scheduleOpt.get();

        if (!medicationSchedule.getUser().equals(user)) {
            throw new IllegalArgumentException("Schedule does not belong to the current user");
        }

        medicationSchedule.setMedicationName(newRequest.getMedication_name());
        medicationSchedule.setScheduledDays(newRequest.getDays());
        medicationSchedule.setScheduledTimes(newRequest.getTimes());

        MedicationSchedule updatedSchedule = medicationScheduleRepository.save(medicationSchedule);
        return updatedSchedule;
    }

    public void deleteSchedule(UUID id) {
        String currentUserEmail = getCurrentUserEmail();
        User user = findUserByEmail(currentUserEmail);

        MedicationSchedule schedule = medicationScheduleRepository.findByIdAndUser(id, user);
        if (schedule == null) {
            logger.error("Schedule not found for ID {} and user {}", id, currentUserEmail);
            throw new IllegalArgumentException("Schedule not found or not owned by user");
        }

        medicationScheduleRepository.delete(schedule);
        logger.info("Deleted schedule with ID {} for user {}", id, currentUserEmail);
    }

    private String getCurrentUserEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", email);
                    return new IllegalArgumentException("User not found");
                });
    }

    private void validateRequest(MedicationScheduleRequest request) {
        if (request.getMedication_name() == null || request.getMedication_name().isBlank()) {
            logger.error("Invalid medication name: {}", request.getMedication_name());
            throw new IllegalArgumentException("Medication name cannot be null or empty");
        }
        if (request.getDays() == null || request.getDays().isEmpty()) {
            logger.error("Invalid scheduled days: {}", request.getDays());
            throw new IllegalArgumentException("Scheduled days cannot be null or empty");
        }
        if (request.getTimes() == null || request.getTimes().isEmpty()) {
            logger.error("Invalid scheduled times: {}", request.getTimes());
            throw new IllegalArgumentException("Scheduled times cannot be null or empty");
        }
    }
}