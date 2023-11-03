package com.hrproject.rabbitmq.consumer;

import com.hrproject.rabbitmq.model.CompanyModel;
import com.hrproject.repository.entity.Company;
import com.hrproject.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyConsumer {

    private final CompanyService companyService;

    @RabbitListener(queues = ("${rabbitmq.company-queue}"))
    public String getCompanyName(CompanyModel companyModel) {

       Company company= companyService.findByIdd(companyModel.getCompanyId());
       String mail=companyModel.getMail()+company.getCompanyName()+".com";



      return null;
    }
}