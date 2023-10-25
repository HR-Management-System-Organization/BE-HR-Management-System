package com.hrproject.rabbitmq.producer;

import com.hrproject.rabbitmq.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterProducer {

    private final RabbitTemplate rabbitTemplate;
    @Value("auth-exchange")
    private String exchange;
    @Value("register-key")
    private String bindingKey;

    public  void sendNewUser(RegisterModel model){
        rabbitTemplate.convertAndSend(exchange,bindingKey,model);
    }

}
