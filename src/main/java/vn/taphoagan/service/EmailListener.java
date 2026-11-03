package vn.taphoagan.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EmailListener {

    private static final Logger log = LoggerFactory.getLogger(EmailListener.class);

    private final JavaMailSender mailSender;
    private final String from;

    public EmailListener(JavaMailSender mailSender,
                         @Value("${spring.mail.username:no-reply@taphoagan.local}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(NotificationService.EmailEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(event.to());
            message.setSubject("[Tạp Hóa Gần] " + event.subject());
            message.setText(event.body());
            mailSender.send(message);
        } catch (Exception ex) {
            log.warn("Email not sent to {}: {}", event.to(), ex.getMessage());
        }
    }
}
