package com.parmida98.school_webpage.email;

import com.parmida98.school_webpage.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    private static final Logger logger = LoggerFactory.getLogger(EmailProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendLoginEmail(String email) {
        String message = "LOGIN|" + email;
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                message
        );
        logger.info("Sent login email event to RabbitMQ for {}", email);
    }

    public void sendAccountCreatedEmail(String email) {
        String message = "ACCOUNT_CREATED|" + email;
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                message
        );
        logger.info("Sent account created email event to RabbitMQ for {}", email);
    }
}

