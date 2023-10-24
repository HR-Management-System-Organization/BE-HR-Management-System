package com.hrproject.service;


import com.hrproject.dto.request.ActivateRequestDto;
import com.hrproject.dto.request.AuthUpdateRequestDto;
import com.hrproject.dto.request.LoginRequestDto;
import com.hrproject.dto.request.RegisterRequestDto;
import com.hrproject.dto.response.RegisterResponseDto;
import com.hrproject.exception.AuthManagerException;
import com.hrproject.exception.ErrorType;
import com.hrproject.manager.IUserManager;
import com.hrproject.mapper.IAuthMapper;
import com.hrproject.rabbitmq.model.MailModel;
import com.hrproject.rabbitmq.producer.ActivationProducer;
import com.hrproject.rabbitmq.producer.MailProducer;
import com.hrproject.rabbitmq.producer.RegisterProducer;
import com.hrproject.repository.IAuthRepository;
import com.hrproject.repository.entity.Auth;
import com.hrproject.repository.enums.EStatus;
import com.hrproject.utility.CodeGenerator;
import com.hrproject.utility.JwtTokenManager;
import com.hrproject.utility.ServiceManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



@Service
public class AuthService extends ServiceManager<Auth, Long> {

    private final IAuthRepository authRepository;

    private final JwtTokenManager jwtTokenManager;

    private final IUserManager userManager;

    private final RegisterProducer registerProducer;

    private final ActivationProducer activationProducer;

    private final MailProducer mailProducer;

    public AuthService(IAuthRepository authRepository, JwtTokenManager jwtTokenManager, IUserManager userManager, RegisterProducer registerProducer, ActivationProducer activationProducer, MailProducer mailProducer) {
        super(authRepository);
        this.authRepository = authRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.userManager = userManager;
        this.registerProducer = registerProducer;
        this.activationProducer = activationProducer;
        this.mailProducer = mailProducer;
    }


}
