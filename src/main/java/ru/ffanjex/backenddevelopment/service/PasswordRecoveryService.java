package ru.ffanjex.backenddevelopment.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.UserRepository;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    private final JavaMailSender emailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${spring.mail.username}")
    private String username;

    private final ConcurrentHashMap<String, String> recoveryCodes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> codeTimestamps = new ConcurrentHashMap<>();

    public String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendRecoveryCode(String email, String code) throws MessagingException {
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        String subject = "Password Recovery Code";
        String body = "<h1>Password Recovery Code</h1>" +
                "<p>Your recovery code is: <b>" + code + "</b></p>";

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(username);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(body, true);

        emailSender.send(mimeMessage);

        recoveryCodes.put(email, code);
        codeTimestamps.put(email, System.currentTimeMillis());
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = recoveryCodes.get(email);
        Long timestamp = codeTimestamps.get(email);

        if (storedCode == null || timestamp == null) {
            return false;
        }

        if (System.currentTimeMillis() - timestamp > 15 * 60 * 1000) {
            recoveryCodes.remove(email);
            codeTimestamps.remove(email);
            return false;
        }

        return storedCode.equals(code);
    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        recoveryCodes.remove(email);
        codeTimestamps.remove(email);
    }

}