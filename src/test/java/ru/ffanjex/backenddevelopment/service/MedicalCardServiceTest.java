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
    void getMedicalCard_ShouldReturnMedicalCard() {
        User user = new User();
        MedicalCard medicalCard = new MedicalCard();
        medicalCard.setUser(user);
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(medicalCardRepository.findByUser(user)).thenReturn(Optional.of(medicalCard));
        MedicalCard result = medicalCardService.getMedicalCard();
        assertEquals(medicalCard, result);
        verify(userService).getUserByEmail("test@example.com");
        verify(medicalCardRepository).findByUser(user);
    }

    @Test
    void editMedicalCard_ShouldUpdateAndReturnMedicalCard() {
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

        MedicalCard updatedMedicalCard = medicalCardService.editMedicalCard(request);
        assertEquals("Test Person", updatedMedicalCard.getFullName());
        assertEquals(180, updatedMedicalCard.getHeight());
        assertEquals(new BigDecimal(75), updatedMedicalCard.getWeight());
        assertEquals("A+", updatedMedicalCard.getBloodType());
        assertEquals("Pollen", updatedMedicalCard.getAllergies());
        assertEquals("Asthma", updatedMedicalCard.getDiseases());
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
