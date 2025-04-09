package ru.ffanjex.backenddevelopment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ffanjex.backenddevelopment.config.JwtTokenProvider;
import ru.ffanjex.backenddevelopment.dto.MedicalCardDTO;
import ru.ffanjex.backenddevelopment.entity.MedicalCard;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.MedicalCardRepository;
import ru.ffanjex.backenddevelopment.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class MedicalCardService {

    private final MedicalCardRepository medicalCardRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MedicalCardDTO createMedicalCard(MedicalCardDTO dto, String token) {
        String email = jwtTokenProvider.getAuthentication(token).getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MedicalCard medicalCard = new MedicalCard();
        medicalCard.setUser(user);
        medicalCard.setFullName(dto.getFullName());
        medicalCard.setHeight(dto.getHeight());
        medicalCard.setWeight(dto.getWeight());
        medicalCard.setBloodType(dto.getBloodType());
        medicalCard.setAllergies(dto.getAllergies());
        medicalCard.setDiseases(dto.getDiseases());

        medicalCardRepository.save(medicalCard);

        return dto;
    }
}
