package ru.ffanjex.backenddevelopment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ffanjex.backenddevelopment.dto.MedicationScheduleRequest;
import ru.ffanjex.backenddevelopment.entity.MedicationSchedule;
import ru.ffanjex.backenddevelopment.service.MedicationScheduleService;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MedicationScheduleControllerTest {
    @Mock
    private MedicationScheduleService medicationScheduleService;

    @InjectMocks
    private MedicationScheduleController medicationScheduleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMedicationSchedule_ShouldReturnListOfSchedules() {
        MedicationSchedule firstSchedule = new MedicationSchedule();
        MedicationSchedule secondSchedule = new MedicationSchedule();
        List<MedicationSchedule> schedules = List.of(firstSchedule,
                secondSchedule);
        when(medicationScheduleService.getUserSchedules()).thenReturn(schedules);
        ResponseEntity<List<MedicationSchedule>> response = medicationScheduleController
                .getUserSchedules();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schedules, response.getBody());
        verify(medicationScheduleService).getUserSchedules();
    }

    @Test
    void addSchedule_ShouldReturnCreatedSchedule() {
        MedicationScheduleRequest request = new MedicationScheduleRequest();
        request.setMedication_name("Ibuprofen");
        request.setDays(Arrays.asList(1, 3, 5));
        request.setTimes(Arrays.asList(LocalTime.of(8, 0),
                LocalTime.of(20, 0)));
        MedicationSchedule createsSchedule = new MedicationSchedule();
        createsSchedule.setMedicationName("Ibuprofen");
        when(medicationScheduleService.addSchedule(request)).thenReturn(createsSchedule);
        ResponseEntity<MedicationSchedule> response = medicationScheduleController
                .addSchedule(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createsSchedule, response.getBody());
        verify(medicationScheduleService).addSchedule(request);
    }

    @Test
    void editMedicationSchedule_ShouldReturnUpdatedSchedule() {
        UUID id = UUID.randomUUID();
        MedicationScheduleRequest request = new MedicationScheduleRequest();
        request.setMedication_name("Paracetamol");
        request.setDays(Arrays.asList(2, 4, 6));
        request.setTimes(Arrays.asList(LocalTime.of(9, 0),
                LocalTime.of(21, 0)));
        MedicationSchedule updatedSchedule = new MedicationSchedule();
        when(medicationScheduleService.editSchedule(id, request))
                .thenReturn(updatedSchedule);
        ResponseEntity<MedicationSchedule> response = medicationScheduleController
                .editSchedule(id, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSchedule, response.getBody());
        verify(medicationScheduleService).editSchedule(id, request);
    }

    @Test
    void editMedicationSchedule_ShouldReturnNotFound() {
        UUID id = UUID.randomUUID();
        MedicationScheduleRequest request = new MedicationScheduleRequest();
        when(medicationScheduleService.editSchedule(id, request))
                .thenReturn(null);
        ResponseEntity<MedicationSchedule> response = medicationScheduleController
                .editSchedule(id, request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(medicationScheduleService).editSchedule(id, request);
    }

    @Test
    void deleteMedicationSchedule_ShouldReturnNoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(medicationScheduleService).deleteSchedule(id);
        ResponseEntity<Void> response = medicationScheduleController.deleteSchedule(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(medicationScheduleService).deleteSchedule(id);
    }
}
