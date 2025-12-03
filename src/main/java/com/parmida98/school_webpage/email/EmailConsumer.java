package com.parmida98.school_webpage.email;

import com.parmida98.school_webpage.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JavaMailSender mailSender;

    public EmailConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handleMessage(String message) {
        logger.info("Received message from RabbitMQ: {}", message);

        // Förväntat format: TYPE|email
        String[] parts = message.split("\\|", 2);
        if (parts.length != 2) {
            logger.warn("Invalid message format: {}", message);
            return;
        }

        String type = parts[0];
        String email = parts[1];

        String subject;
        String body;

        switch (type) {
            case "LOGIN" -> {
                subject = "Login notification";
                body = "Hi!\n\nYou have signed in to School Webpage";
            }
            case "ACCOUNT_CREATED" -> {
                subject = "Account created";
                body = "Hi!\n\nYour account has been created for School Webpage";
            }
            default -> {
                logger.warn("Unknown email event type: {}", type);
                return;
            }
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        try {
            mailSender.send(mailMessage);
            logger.info("Email sent successfully to {} for event {}", email, type);
        } catch (MailException e) {
            logger.error("Failed to send email to {}: {}", email, e.getMessage(), e);
        }
    }
}

