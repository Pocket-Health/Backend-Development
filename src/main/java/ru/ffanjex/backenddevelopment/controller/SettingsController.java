package ru.ffanjex.backenddevelopment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.PasswordChangeRequest;
import ru.ffanjex.backenddevelopment.service.SettingsService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {
    private final SettingsService settingsService;

    @GetMapping("/settings")
    public ResponseEntity<Map<String, Boolean>> getSettings() {
        boolean notification = settingsService.getNotificationStatus();
        Map<String, Boolean> response = new HashMap<>();
        response.put("notification", notification);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        settingsService.changePassword(passwordChangeRequest.getPassword());
        return ResponseEntity.ok("Password changes successfully");
    }

    @PostMapping("/enable_notification")
    public ResponseEntity<?> enableNotification() {
        settingsService.setNotificationStatus(true);
        return ResponseEntity.ok("Notification enables");
    }

    @PostMapping("/disable_notification")
    public ResponseEntity<?> disableNotification() {
        settingsService.setNotificationStatus(false);
        return ResponseEntity.ok("Notification disables");
    }
}
