package com.hrproject.service;


import com.hrproject.dto.request.AuthUpdateRequestDto;
import com.hrproject.dto.request.LoginRequestDto;
import com.hrproject.dto.request.RegisterGuestRequestDto;
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

import java.util.List;
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

    @Transactional
    public RegisterResponseDto registerWithRabbitMq(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        if (authRepository.existsByUsername(dto.getUsername())) {
            throw new AuthManagerException(ErrorType.USERNAME_ALREADY_EXIST);
        }
        save(auth);
        //rabbit mq ile haberleştireceğiz
        registerProducer.sendNewUser(IAuthMapper.INSTANCE.toRegisterModel(auth));


        //register token olusturma
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
        String token = jwtTokenManager.createToken(auth.getId())
                .orElseThrow(() -> new AuthManagerException(ErrorType.INVALID_TOKEN));

        responseDto.setToken(token);
        // mail atma işlemi için mail servis ile haberleşilecek
        MailModel mailModel = IAuthMapper.INSTANCE.toMailModel(auth);
        mailModel.setToken(token);

        mailProducer.sendMail(mailModel);
        return responseDto;
    }

    @Transactional
    public RegisterResponseDto registerWithRabbitMq(RegisterGuestRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        if (authRepository.existsByUsername(dto.getUsername())) {
            throw new AuthManagerException(ErrorType.USERNAME_ALREADY_EXIST);
        }
        save(auth);
        //rabbit mq ile haberleştireceğiz
        registerProducer.sendNewUser(IAuthMapper.INSTANCE.toRegisterModel(auth));


        //register token olusturma
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
        String token = jwtTokenManager.createToken(auth.getId())
                .orElseThrow(() -> new AuthManagerException(ErrorType.INVALID_TOKEN));

        responseDto.setToken(token);
        // mail atma işlemi için mail servis ile haberleşilecek
        MailModel mailModel = IAuthMapper.INSTANCE.toMailModel(auth);
        mailModel.setToken(token);

        mailProducer.sendMail(mailModel);
        return responseDto;
    }
    public String login(LoginRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (optionalAuth.isEmpty()) {
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        }
        if (!optionalAuth.get().getStatus().equals(EStatus.ACTIVE)) {
            throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }

        return jwtTokenManager.createToken(optionalAuth.get().getId(), optionalAuth.get().getRole())
                .orElseThrow(() -> new AuthManagerException(ErrorType.TOKEN_NOT_CREATED));
    }

    public List<Auth> findAll() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return authRepository.findAll();
    }
    public String updateAuth(AuthUpdateRequestDto dto) {
        Optional<Auth> auth = findById(dto.getId());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setEmail(dto.getEmail());
        auth.get().setUsername(dto.getUsername());
        update(auth.get());
        return "Guncelleme başarılı";
    }

    @Transactional
    public String deleteAuth(String token) {
        Optional<Long> id = jwtTokenManager.getIdFromToken(token);
        if (id.isEmpty()) {
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<Auth> auth = findById(id.get());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (auth.get().getStatus().equals(EStatus.DELETED)) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND, "Hesap zaten silinmiş");
        }
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        userManager.deleteById("Bearer " + token);
        return id + "id li kullanıcı başarıyla slindi";
    }


}