package az.ibrahim.libraryapi.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    @Value("${application.notification.from}")
    private String from;

    @Override
    public void sendBookCreatedNotification(String email, String bookTitle) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(email);
        message.setSubject("New Book Created");
        message.setText("A new book has been created: " + bookTitle);

        mailSender.send(message);

        log.info("Book creation email sent to {}", email);
    }
}