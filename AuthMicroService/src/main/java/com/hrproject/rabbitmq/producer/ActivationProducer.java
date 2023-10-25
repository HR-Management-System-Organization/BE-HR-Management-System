package com.hrproject.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationProducer {

    private final RabbitTemplate rabbitTemplate;
    @Value("auth-exchange")
    private String exchange;
    @Value("activation-key")
    private String bindingKey;


    public  void activateStatus(String token){
        rabbitTemplate.convertAndSend(exchange,bindingKey,token);
    }

}
