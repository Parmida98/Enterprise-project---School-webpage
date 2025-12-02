package com.parmida98.school_webpage.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_NAME = "email-queue";
    public static final String EXCHANGE_NAME = "email-exchange";
    public static final String ROUTING_KEY = "email.routing";

    // Metoden skapar och returnerar en Queue-instans som Spring registrerar hos RabbitMQ
    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    // kommer att ta emot meddelanden och skicka dem vidare till rätt kö baserat på routing key
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder
                .bind(emailQueue)
                .to(emailExchange)
                .with(ROUTING_KEY);
    }
}
