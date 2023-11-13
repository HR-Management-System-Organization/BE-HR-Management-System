package com.hrproject.rabbitmq.consumer;

import com.hrproject.rabbitmq.model.ExpenseModel;
import com.hrproject.repository.entity.Company;

import com.hrproject.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseConsumer {

    private final CompanyService service;
    @RabbitListener(queues = ("${rabbitmq.expense-queue}"))
    public void expense(ExpenseModel model) {
        service.maasekle(model.getSayi(),model.getMaas(), model.getName(), model.getSurname(), model.getCompany());
    }
}