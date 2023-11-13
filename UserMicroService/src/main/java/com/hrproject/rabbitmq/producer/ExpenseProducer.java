package com.hrproject.rabbitmq.producer;

import com.hrproject.rabbitmq.model.CompanyModel;
import com.hrproject.rabbitmq.model.ExpenseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;



    @Value("${rabbitmq.expense-binding-key}")
    private String expenseBindingKey;

   public void sendCompany(ExpenseModel model) {
        rabbitTemplate.convertAndSend(exchange, expenseBindingKey, model);
    }
}
