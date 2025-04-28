package ru.ffanjex.backenddevelopment.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.UserRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordRecoveryServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordRecoveryService passwordRecoveryService;

    @Captor
    private ArgumentCaptor<MailMessage> mailMessageArgumentCaptor;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("oldPassword");
    }

    @Test
    void generateCode_ShouldReturnCode() {
        String code = passwordRecoveryService.generateCode();
        assertEquals(6, code.length());
        assertTrue(code.matches("\\d{6}"));
    }

    @Test
    void sendRecoveryCode_ShouldSendEmailAndStoreCode() throws MessagingException {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        org.springframework.test.util.ReflectionTestUtils
                .setField(passwordRecoveryService, "username", "noreply@example.com");
        String code = passwordRecoveryService.generateCode();
        passwordRecoveryService.sendRecoveryCode("test@example.com", code);
        verify(mailSender, times(1)).send(any(MimeMessage.class));
        assertTrue(passwordRecoveryService.verifyCode("test@example.com", code));
    }

    @Test
    void sendRecoveryCode_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        String code = passwordRecoveryService.generateCode();
        assertThrows(RuntimeException.class, () -> {
            passwordRecoveryService.sendRecoveryCode("notfound@example.com", code);
        });
    }

    @Test
    void verifyCode_ShouldReturnFalse_WhenCodeExpired() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        String code = passwordRecoveryService.generateCode();
        passwordRecoveryService.verifyCode("test@example.com", code);
        ConcurrentHashMap<String, Long> timestamps = (ConcurrentHashMap<String, Long>)
                ReflectionTestUtils.getField(passwordRecoveryService, "codeTimestamps");
        timestamps.put("test@example.com", System.currentTimeMillis() - (16 * 60 * 1000));
        boolean result = passwordRecoveryService.verifyCode("test@example.com", code);
        assertFalse(result);
    }

    @Test
    void verifyCode_ShouldReturnFalse_WhenNoCodeStored() {
        boolean result = passwordRecoveryService.verifyCode("test@example.com", "123456");
        assertFalse(result);
    }

    @Test
    void resetPassword_ShouldChangePasswordAndClearCodes() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        String code = passwordRecoveryService.generateCode();
        ConcurrentHashMap<String, String> recoveryCodes = (ConcurrentHashMap<String, String>)
                ReflectionTestUtils.getField(passwordRecoveryService, "recoveryCodes");
        ConcurrentHashMap<String, Long> codeTimestamps = (ConcurrentHashMap<String, Long>)
                ReflectionTestUtils.getField(passwordRecoveryService, "codeTimestamps");
        recoveryCodes.put("test@example.com", code);
        codeTimestamps.put("test@example.com", System.currentTimeMillis());
        passwordRecoveryService.resetPassword("test@example.com", "newPassword");
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository).save(user);
        assertNull(recoveryCodes.get("test@example.com"));
        assertNull(codeTimestamps.get("test@example.com"));
    }
}
