package ru.ffanjex.backenddevelopment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.ffanjex.backenddevelopment.dto.MedicalCardRequest;
import ru.ffanjex.backenddevelopment.dto.MedicalCardResponse;
import ru.ffanjex.backenddevelopment.entity.MedicalCard;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.MedicalCardRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalCardServiceTest {

    @Mock
    private MedicalCardRepository medicalCardRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MedicalCardService medicalCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("test@example.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getMedicalCard_ShouldReturnMedicalCardResponse() {
        User user = new User();
        MedicalCard medicalCard = new MedicalCard();
        medicalCard.setUser(user);
        medicalCard.setFullName("Test User");
        medicalCard.setHeight(180);
        medicalCard.setWeight(new BigDecimal("80"));
        medicalCard.setBloodType("B+");
        medicalCard.setAllergies("None");
        medicalCard.setDiseases("None");
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(medicalCardRepository.findByUser(user)).thenReturn(Optional.of(medicalCard));
        MedicalCardResponse response = medicalCardService.getMedicalCard();
        assertEquals("Test User", response.getFullName());
        assertEquals(180, response.getHeight());
        assertEquals(new BigDecimal("80"), response.getWeight());
        assertEquals("B+", response.getBloodType());
        assertEquals("None", response.getAllergies());
        assertEquals("None", response.getDiseases());
    }

    @Test
    void editMedicalCard_ShouldUpdateAndReturnMedicalCardResponse() {
        User user = new User();
        MedicalCard medicalCard = new MedicalCard();
        medicalCard.setUser(user);
        MedicalCardRequest request = new MedicalCardRequest();
        request.setFullname("Test Person");
        request.setHeight(180);
        request.setWeight(new BigDecimal(75));
        request.setBlood_type("A+");
        request.setAllergies("Pollen");
        request.setDiseases("Asthma");
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(medicalCardRepository.findByUser(user)).thenReturn(Optional.of(medicalCard));
        when(medicalCardRepository.save(any(MedicalCard.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MedicalCardResponse response = medicalCardService.editMedicalCard(request);
        assertEquals("Test Person", response.getFullName());
        assertEquals(180, response.getHeight());
        assertEquals(new BigDecimal(75), response.getWeight());
        assertEquals("A+", response.getBloodType());
        assertEquals("Pollen", response.getAllergies());
        assertEquals("Asthma", response.getDiseases());

        verify(medicalCardRepository).save(medicalCard);
    }

    @Test
    void editMedicalCard_ShouldThrowException_WhenMedicalCardNotFound() {
        User user = new User();
        MedicalCardRequest request = new MedicalCardRequest();
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(medicalCardRepository.findByUser(user)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            medicalCardService.editMedicalCard(request);
        });
        assertEquals("Medical card not found", exception.getMessage());
    }
}