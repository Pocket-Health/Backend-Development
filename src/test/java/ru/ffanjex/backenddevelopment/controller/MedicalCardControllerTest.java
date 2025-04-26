package ru.ffanjex.backenddevelopment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ffanjex.backenddevelopment.dto.MedicalCardRequest;
import ru.ffanjex.backenddevelopment.entity.MedicalCard;
import ru.ffanjex.backenddevelopment.service.MedicalCardService;

import java.math.BigDecimal;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MedicalCardControllerTest {
    @Mock
    private MedicalCardService medicalCardService;

    @InjectMocks
    private MedicalCardController medicalCardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMedicalCard_ShouldReturnOk() {
        MedicalCard expectedMedicalCard = new MedicalCard();
        expectedMedicalCard.setId(UUID.randomUUID());
        expectedMedicalCard.setFullName("Test Person");
        when(medicalCardService.getMedicalCard()).thenReturn(expectedMedicalCard);
        ResponseEntity<MedicalCard> response = medicalCardController.getMedicalCard();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMedicalCard, response.getBody());
        verify(medicalCardService).getMedicalCard();
    }

    @Test
    void editMedicalCard_ShouldReturnOk() {
        MedicalCardRequest request = new MedicalCardRequest();
        request.setFullname("Test Person");
        request.setHeight(170);
        request.setWeight(new BigDecimal(65));
        request.setBlood_type("A+");
        request.setAllergies("None");
        request.setDiseases("None");
        MedicalCard updatedMedicalCard = new MedicalCard();
        updatedMedicalCard.setId(UUID.randomUUID());
        updatedMedicalCard.setFullName("Test Person");
        when(medicalCardService.editMedicalCard(request)).thenReturn(updatedMedicalCard);
        ResponseEntity<MedicalCard> response = medicalCardController.editMedicalCard(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMedicalCard, response.getBody());
        verify(medicalCardService).editMedicalCard(request);
    }
}


