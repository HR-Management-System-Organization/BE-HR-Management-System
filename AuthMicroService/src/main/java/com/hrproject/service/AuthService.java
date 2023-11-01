package com.hrproject.service;

import com.hrproject.dto.request.AuthUpdateRequestDto;
import com.hrproject.dto.request.LoginRequestDto;
import com.hrproject.dto.request.RegisterGuestRequestDto;
import com.hrproject.dto.request.RegisterRequestDto;
import com.hrproject.dto.response.RegisterResponseDto;
import com.hrproject.exception.AuthManagerException;
import com.hrproject.exception.ErrorType;
import com.hrproject.mapper.IAuthMapper;
import com.hrproject.rabbitmq.model.MailModel;
import com.hrproject.rabbitmq.model.RegisterModel;
import com.hrproject.rabbitmq.producer.ActivationProducer;
import com.hrproject.rabbitmq.producer.MailProducer;
import com.hrproject.rabbitmq.producer.RegisterProducer;
import com.hrproject.repository.IAuthRepository;
import com.hrproject.repository.entity.Auth;
import com.hrproject.repository.enums.ERole;
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

    private final RegisterProducer registerProducer;

    private final ActivationProducer activationProducer;

    private final MailProducer mailProducer;

    public AuthService(IAuthRepository authRepository, JwtTokenManager jwtTokenManager, RegisterProducer registerProducer, ActivationProducer activationProducer, MailProducer mailProducer) {

        super(authRepository);

        this.authRepository = authRepository;

        this.jwtTokenManager = jwtTokenManager;

        this.registerProducer = registerProducer;

        this.activationProducer = activationProducer;

        this.mailProducer = mailProducer;
    }

    @Transactional
    public RegisterResponseDto registerWithRabbitMq(RegisterRequestDto dto) {

        System.out.println("burdasin");

        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        auth.setRole(ERole.COMPANY_MANAGER);


        save(auth);

        // rabbitmq ile haberleştireceğiz
        registerProducer.sendNewUser(IAuthMapper.INSTANCE.toRegisterModel(auth));

        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.toRegisterResponseDto(auth);

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

        String token = jwtTokenManager.createToken(auth.getId(), auth.getActivationCode())
                .orElseThrow(() -> new AuthManagerException(ErrorType.INVALID_TOKEN));

        responseDto.setToken(token);

        String link = "http://localhost:7071/api/v1/auth/activation?token=" + token;

        // mail atma işlemi için mail servis ile haberleşilecek
        MailModel mailModel = MailModel.builder()
                .email(dto.getEmail())
                .subject("Aktivasyon Linki")
                .text("Aktivasyon kodu ->   " + link)
                .build();

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

        return "Güncelleme Başarılı.";
    }


    public String activation(String token) {

        if (!jwtTokenManager.verifyToken(token)) {
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }

        if (jwtTokenManager.getActivationCode(token).isEmpty()) {
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }

        if (jwtTokenManager.getIdFromToken(token).isEmpty()) {
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }

        if (authRepository.findById(jwtTokenManager.getIdFromToken(token).get()).isEmpty()) {
            System.out.println("burdasin0");
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }

        if (!authRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get().getId().equals(jwtTokenManager.getIdFromToken(token).get())) {
            System.out.println(authRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get());
            System.out.println("burdasin");

            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }

        System.out.println("burdasin2");

        Auth userProfile = authRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get();

        if (userProfile.getActivationCode().equals(jwtTokenManager.getActivationCode(token).get())) {
            try {

                userProfile.setStatus(EStatus.ACTIVE);
                RegisterModel registerModel = IAuthMapper.INSTANCE.toRegisterModel(userProfile);
                activationProducer.activateStatus(userProfile.getUsername());
                return update(userProfile).getStatus().toString();

            } catch (Exception e) {

                throw new AuthManagerException(ErrorType.INTERNAL_ERROR_SERVER);

            }
        }

        return "Başarısız!";
    }


}
