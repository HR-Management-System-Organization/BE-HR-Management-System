package com.hrproject.rabbitmq.producer;

import com.hrproject.rabbitmq.model.Company2Model;
import com.hrproject.rabbitmq.model.CompanyModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Company2Producer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    @Value("${rabbitmq.company-binding-key2}")
    private String companyBindingKey2;

   public void sendCompany(Company2Model model) {
       System.out.println("burdayiz company2 prducer");
        rabbitTemplate.convertAndSend(exchange, companyBindingKey2, model);
       System.out.println("burdayiz company2 prducer2");
    }
}
