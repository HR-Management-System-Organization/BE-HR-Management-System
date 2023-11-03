package com.hrproject.rabbitmq.producer;

import com.hrproject.rabbitmq.model.CompanyModel;
import com.hrproject.rabbitmq.model.MailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    @Value("${rabbitmq.company-binding-key}")
    private String bindingKey;

   public void sendCompany(CompanyModel model) {
        rabbitTemplate.convertAndSend(exchange, bindingKey, model);
    }
}
