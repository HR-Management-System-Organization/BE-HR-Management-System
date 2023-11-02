package com.hrproject.rabbitmq.producer;

import com.hrproject.rabbitmq.model.CreateCommentModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCommentProducer {
    private final RabbitTemplate rabbitTemplate;
    private final String exchange = "comment-exchange";
    private final String createCommentBindingKey = "comment-bindingkey";
    public Object createComment(CreateCommentModel model) {

        return rabbitTemplate.convertSendAndReceive(exchange, createCommentBindingKey, model);
    }
}
