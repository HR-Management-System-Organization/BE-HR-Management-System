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

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    @Value("${rabbitmq.company-queue}")
    private String companyQueueName;

    @Value("${rabbitmq.company-binding-key}")
    private String companyBindingKey;


    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue companyQueue() {
        return new Queue(companyQueueName);
    }

    @Bean
    public Binding bindingCompany(Queue companyQueue, DirectExchange exchange) {
        return BindingBuilder.bind(companyQueue).to(exchange).with(companyBindingKey);
    }
}