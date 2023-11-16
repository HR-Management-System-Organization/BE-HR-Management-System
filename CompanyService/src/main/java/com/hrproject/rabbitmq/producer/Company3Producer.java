package com.hrproject.rabbitmq.producer;

import com.hrproject.rabbitmq.model.Company3Model;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Company3Producer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    @Value("${rabbitmq.company-binding-key3}")
    private String companyBindingKey3;

   public void sendCompany(Company3Model model) {
        rabbitTemplate.convertAndSend(exchange, companyBindingKey3, model);
    }
}
