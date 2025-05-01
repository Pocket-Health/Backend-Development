package ru.ffanjex.backenddevelopment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ffanjex.backenddevelopment.dto.MedicalCardRequest;
import ru.ffanjex.backenddevelopment.dto.MedicalCardResponse;
import ru.ffanjex.backenddevelopment.service.MedicalCardService;

import java.math.BigDecimal;
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
        MedicalCardResponse expectedResponse = new MedicalCardResponse();
        expectedResponse.setFullName("Test Person");
        expectedResponse.setHeight(180);
        expectedResponse.setWeight(new BigDecimal("70"));
        expectedResponse.setBloodType("O+");
        expectedResponse.setAllergies("None");
        expectedResponse.setDiseases("None");

        when(medicalCardService.getMedicalCard()).thenReturn(expectedResponse);

        ResponseEntity<MedicalCardResponse> response = medicalCardController.getMedicalCard();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
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

        MedicalCardResponse responseDto = new MedicalCardResponse();
        responseDto.setFullName("Test Person");
        responseDto.setHeight(170);
        responseDto.setWeight(new BigDecimal(65));
        responseDto.setBloodType("A+");
        responseDto.setAllergies("None");
        responseDto.setDiseases("None");
        when(medicalCardService.editMedicalCard(request)).thenReturn(responseDto);
        ResponseEntity<MedicalCardResponse> response = medicalCardController.editMedicalCard(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(medicalCardService).editMedicalCard(request);
    }
}
