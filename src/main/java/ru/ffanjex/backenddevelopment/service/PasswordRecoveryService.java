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

        String subject = "Код для восстановления пароля";
        String body = """
        <html>
        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
            <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; padding: 30px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                <h2 style="color: #333;">🔐 Восстановление пароля</h2>
                <p style="font-size: 16px; color: #555;">
                    Вы запросили восстановление пароля. Ваш код:
                </p>
                <p style="font-size: 24px; font-weight: bold; color: #007BFF; text-align: center; margin: 20px 0;">
                    %s
                </p>
                <p style="font-size: 14px; color: #777;">
                    Пожалуйста, введите этот код в приложении. Он действителен в течение 15 минут.
                </p>
                <hr style="margin: 30px 0;">
                <p style="font-size: 12px; color: #aaa;">
                    Если вы не запрашивали восстановление, просто проигнорируйте это письмо.
                </p>
            </div>
        </body>
        </html>
        """.formatted(code);

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