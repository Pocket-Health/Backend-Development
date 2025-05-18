package ru.ffanjex.backenddevelopment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.ffanjex.backenddevelopment.dto.MedicationScheduleRequest;
import ru.ffanjex.backenddevelopment.dto.MedicationScheduleResponse;
import ru.ffanjex.backenddevelopment.entity.MedicationSchedule;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.MedicationScheduleRepository;
import ru.ffanjex.backenddevelopment.repository.UserRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicationScheduleServiceTest {

    @Mock
    private MedicationScheduleRepository medicationScheduleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MedicationScheduleService medicationScheduleService;

    private User user;
    private MedicationScheduleRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("test@example.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        user = new User();
        user.setEmail("test@example.com");
        request = new MedicationScheduleRequest();
        request.setMedication_name("Aspirin");
        request.setDays(List.of(1, 2, 3));
        request.setTimes(List.of(LocalTime.of(8, 0), LocalTime.of(18, 0)));
    }

    @Test
    void getUserSchedules_ShouldReturnSchedules() {
        MedicationSchedule schedule = new MedicationSchedule();
        schedule.setId(UUID.randomUUID());
        schedule.setMedicationName("Aspirin");
        schedule.setScheduledDays(List.of(1, 2));
        schedule.setScheduledTimes(List.of(LocalTime.of(8, 0)));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(medicationScheduleRepository.findByUser(user)).thenReturn(List.of(schedule));
        List<MedicationScheduleResponse> schedules = medicationScheduleService.getUserScheduleResponses();
        assertEquals(1, schedules.size());
        assertEquals("Aspirin", schedules.get(0).getMedicationName());
        verify(medicationScheduleRepository).findByUser(user);
    }

    @Test
    void addSchedule_ShouldAddScheduleSuccessfully() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        MedicationSchedule schedule = new MedicationSchedule();
        schedule.setMedicationName("Aspirin");
        schedule.setScheduledDays(List.of(1, 2, 3));
        schedule.setScheduledTimes(List.of(LocalTime.of(8, 0), LocalTime.of(18, 0)));
        schedule.setUser(user);
        when(medicationScheduleRepository.save(any(MedicationSchedule.class))).thenReturn(schedule);
        MedicationSchedule addedSchedule = medicationScheduleService.addSchedule(request);
        assertEquals("Aspirin", addedSchedule.getMedicationName());
        assertEquals(2, addedSchedule.getScheduledTimes().size());
        verify(medicationScheduleRepository).save(any(MedicationSchedule.class));
    }

    @Test
    void editSchedule_ShouldUpdateAndReturnSchedule() {
        UUID scheduleId = UUID.randomUUID();
        MedicationSchedule existingSchedule = new MedicationSchedule();
        existingSchedule.setId(scheduleId);
        existingSchedule.setMedicationName("Aspirin");
        existingSchedule.setUser(user);
        MedicationScheduleRequest newRequest = new MedicationScheduleRequest();
        newRequest.setMedication_name("Paracetamol");
        newRequest.setDays(List.of(2, 4));
        newRequest.setTimes(List.of(LocalTime.of(10, 0), LocalTime.of(16, 0)));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(medicationScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(existingSchedule));
        when(medicationScheduleRepository.save(any(MedicationSchedule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MedicationSchedule updatedSchedule = medicationScheduleService.editSchedule(scheduleId, newRequest);
        assertEquals("Paracetamol", updatedSchedule.getMedicationName());
        assertEquals(2, updatedSchedule.getScheduledDays().size());
        assertEquals(LocalTime.of(10, 0), updatedSchedule.getScheduledTimes().get(0));
        verify(medicationScheduleRepository).save(existingSchedule);
    }

    @Test
    void editSchedule_ShouldThrowException_WhenScheduleNotFound() {
        UUID scheduleId = UUID.randomUUID();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(medicationScheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            medicationScheduleService.editSchedule(scheduleId, request);
        });
        assertEquals("Schedule not found", exception.getMessage());
    }

    @Test
    void deleteSchedule_ShouldDeleteSuccessfully() {
        UUID scheduleId = UUID.randomUUID();
        MedicationSchedule schedule = new MedicationSchedule();
        schedule.setId(scheduleId);
        schedule.setUser(user);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(medicationScheduleRepository.findByIdAndUser(scheduleId, user)).thenReturn(schedule);
        medicationScheduleService.deleteSchedule(scheduleId);
        verify(medicationScheduleRepository).delete(schedule);
    }

    @Test
    void deleteSchedule_ShouldThrowException_WhenScheduleNotFound() {
        UUID scheduleId = UUID.randomUUID();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(medicationScheduleRepository.findByIdAndUser(scheduleId, user)).thenReturn(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            medicationScheduleService.deleteSchedule(scheduleId);
        });
        assertEquals("Schedule not found or not owned by user", exception.getMessage());
    }
}
