package com.hrproject.rabbitmq.consumer;

import com.hrproject.rabbitmq.model.Company2Model;
import com.hrproject.rabbitmq.model.Company3Model;

import com.hrproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Company3Consumer {

    private final UserService userService;

    @RabbitListener(queues = ("${rabbitmq.company-queue3}"))
    public void companysearc(Company3Model model) {
        userService.companyidgirrabit(model);


        }
    }

