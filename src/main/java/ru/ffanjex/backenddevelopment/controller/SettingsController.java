package ru.ffanjex.backenddevelopment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Settings", description = "Endpoints for user settings management")
public class SettingsController {
    private final SettingsService settingsService;

    @Operation(summary = "Get notification settings", description = "Returns current notification settings for the user")
    @ApiResponse(responseCode = "200", description = "Notification setting retrieved",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class)))
    @GetMapping("/settings")
    public ResponseEntity<Map<String, Boolean>> getSettings() {
        boolean notification = settingsService.getNotificationStatus();
        Map<String, Boolean> response = new HashMap<>();
        response.put("notification", notification);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Change user password", description = "Changes the current user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password", content = @Content)
    })
    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {

        if (passwordChangeRequest.getOldPassword().equals(passwordChangeRequest.getNewPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body("New password must be different from the old password");
        }

        try {
            settingsService.changePassword(passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());
            return ResponseEntity.ok("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @Operation(summary = "Enable notifications", description = "Turns on notifications for the user")
    @ApiResponse(responseCode = "200", description = "Notifications enabled")
    @PostMapping("/enable_notification")
    public ResponseEntity<?> enableNotification() {
        settingsService.setNotificationStatus(true);
        return ResponseEntity.ok("Notification enables");
    }

    @Operation(summary = "Disable notifications", description = "Turns off notifications for the user")
    @ApiResponse(responseCode = "200", description = "Notifications disabled")
    @PostMapping("/disable_notification")
    public ResponseEntity<?> disableNotification() {
        settingsService.setNotificationStatus(false);
        return ResponseEntity.ok("Notification disables");
    }
}
