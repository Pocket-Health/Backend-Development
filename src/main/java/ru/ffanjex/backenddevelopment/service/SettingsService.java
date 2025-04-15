package ru.ffanjex.backenddevelopment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SettingsService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void changePassword(String newPassword) {
        String email = getCurrentUserEmail();
        User user = userService.getUserByEmail(email);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean getNotificationStatus() {
        String email = getCurrentUserEmail();
        User user = userService.getUserByEmail(email);
        return user.getNotificationsEnabled();
    }

    @Transactional
    public void setNotificationStatus(boolean enabled) {
        String email = getCurrentUserEmail();
        User user = userService.getUserByEmail(email);
        user.setNotificationsEnabled(enabled);
        userRepository.save(user);
    }

    private String getCurrentUserEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
