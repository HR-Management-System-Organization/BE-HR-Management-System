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
    @Value("auth-exchange")
    private String exchange;

    @Value("register-key")
    private String registerBindingKey;

    @Value("register-queue")
    private String registerQueueName;

    @Value("activation-key")
    private String activationBindingKey;

    @Value("queue-activation")
    private String activationQueueName;

    @Value("mail-queue")
    private String mailQueueName;

    @Value("mail-binding-key")
    private String mailBindingKey;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue registerQueue() {
        return new Queue(registerQueueName);
    }

    @Bean
    public Binding bindingRegister(Queue registerQueue, DirectExchange exchange) {
        return BindingBuilder.bind(registerQueue).to(exchange).with(registerBindingKey);
    }

    @Bean
    public Queue activationQueue() {
        return new Queue(activationQueueName);
    }

    @Bean
    public Binding bindingActivation(Queue activationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(activationQueue).to(exchange).with(activationBindingKey);
    }

    @Bean
    public Queue mailQueue() {
        return new Queue(mailQueueName);
    }

    @Bean
    public Binding bindingMail(Queue mailQueue, DirectExchange exchange) {
        return BindingBuilder.bind(mailQueue).to(exchange).with(mailBindingKey);
    }
}