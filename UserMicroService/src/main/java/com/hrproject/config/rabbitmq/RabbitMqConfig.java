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

    @Value("${rabbitmq.register-binding-key}")
    private String registerBindingKey;  //unique --> her bir mesaj isteğine göre özel üretilmelidir

    @Value("${rabbitmq.register-queue}")
    private String registerQueueName;  //unique --> her bir mesaj isteğine göre özel üretilmelidir

    @Value("${rabbitmq.activation-binding-key}")
    private String activationBindingKey;

    @Value("${rabbitmq.activation-queue}")
    private String activationQueueName;

    @Value("${rabbitmq.mail-queue}")
    private String mailQueueName;

    @Value("${rabbitmq.mail-binding-key}")
    private String mailBindingKey;

    @Value("${rabbitmq.company-queue}")
    private String companyQueueName;

    @Value("${rabbitmq.company-binding-key}")
    private String companyBindingKey;
    @Value("${rabbitmq.expense-queue}")
    private String expenseQueueName;

    @Value("${rabbitmq.expense-binding-key}")
    private String expenseBindingKey;
    @Value("${rabbitmq.company-queue2}")
    private String companyQueueName2;

    @Value("${rabbitmq.company-binding-key2}")
    private String companyBindingKey2;




    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue companyQueue2() {
        return new Queue(companyQueueName2);
    }

    @Bean
    public Binding bindingCompany2(Queue companyQueue, DirectExchange exchange) {
        return BindingBuilder.bind(companyQueue).to(exchange).with(companyBindingKey2);
    }

    @Bean
    public Queue expenseQueue() {
        return new Queue(expenseQueueName);
    }

    @Bean
    public Binding bindingExpense(Queue expenseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(expenseQueue).to(exchange).with(expenseBindingKey);
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
    @Bean
    public Queue companyQueue() {
        return new Queue(companyQueueName);
    }

    @Bean
    public Binding bindingCompany(Queue companyQueue, DirectExchange exchange) {
        return BindingBuilder.bind(companyQueue).to(exchange).with(companyBindingKey);
    }
}