package com.hrproject.rabbitmq.consumer;

import com.hrproject.rabbitmq.model.MailModel;
import com.hrproject.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailConsumer {
    private final MailService mailService;

    @RabbitListener(queues = "mail-queue")
    public void sendEmail(MailModel model) {
        mailService.rabbitMessage(model);
    }
}
