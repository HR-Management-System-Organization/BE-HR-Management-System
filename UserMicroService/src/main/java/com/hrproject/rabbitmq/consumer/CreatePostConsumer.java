package com.hrproject.rabbitmq.consumer;

import com.hrproject.dto.response.UserProfileResponseDto;
import com.hrproject.rabbitmq.model.CreatePostModel;
import com.hrproject.repository.entity.UserProfile;
import com.hrproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatePostConsumer {

    //http:10.20.123.23/auth/all-users

    private final UserService userService;

    @RabbitListener(queues = "post-queue")
    public Object createPost(CreatePostModel createPostModel) { // authId

            Optional<UserProfile> userProfile = userService.findByUserWithAuthId(createPostModel.getAuthId());

        UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.builder()
                .userId(userProfile.get().getId())
                .userAvatar(userProfile.get().getAvatar())
                .username(userProfile.get().getUsername())
                .build();

        return userProfileResponseDto;
    }
}