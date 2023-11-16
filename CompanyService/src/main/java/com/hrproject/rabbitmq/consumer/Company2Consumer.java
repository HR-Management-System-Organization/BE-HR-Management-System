package com.hrproject.rabbitmq.consumer;

import com.hrproject.rabbitmq.model.Company2Model;
import com.hrproject.rabbitmq.model.ExpenseModel;
import com.hrproject.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Company2Consumer {

    private final CompanyService service;

    @RabbitListener(queues = ("${rabbitmq.company-queue2}"))
    public void companysearc(Company2Model model) {
        service.companycreate(model);

        }
    }

