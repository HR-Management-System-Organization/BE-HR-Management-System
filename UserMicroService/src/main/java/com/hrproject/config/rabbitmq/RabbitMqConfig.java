package com.hrproject.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.user-exchange}")
    private String exchange;

    @Value("${rabbitmq.register-queue}")
    private String registerQueueName;

    @Value("${rabbitmq.activation-queue}")
    private String activationQueueName;

    @Bean
    public DirectExchange userExchange() {

        return new DirectExchange(exchange);
    }

    @Bean
    Queue registerQueue() {

        return new Queue(registerQueueName);
    }

    @Bean
    public Queue activationQueue() {

        return new Queue(activationQueueName);
    }
}