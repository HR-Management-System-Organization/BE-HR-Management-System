package com.hrproject.service;

import com.hrproject.dto.request.UserLoginDto;
import com.hrproject.dto.request.UserSaveRequestDto;
import com.hrproject.exception.ErrorType;
import com.hrproject.exception.UserManagerException;
import com.hrproject.mapper.IUserMapper;
import com.hrproject.rabbitmq.model.RegisterModel;
import com.hrproject.repository.IUserRepository;
import com.hrproject.repository.entity.UserProfile;
import com.hrproject.repository.enums.EGender;
import com.hrproject.repository.enums.ERole;
import com.hrproject.repository.enums.EStatus;
import com.hrproject.utility.JwtTokenManager;
import com.hrproject.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends ServiceManager<UserProfile, Long> { //extends ServiceManager<UserProfile, String> {

    private final IUserRepository userRepository;

    private final JwtTokenManager jwtTokenManager;

    private final IUserMapper userMapper;


    public UserService(IUserRepository userRepository, JwtTokenManager jwtTokenManager, IUserMapper userMapper) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.userMapper = userMapper;
    }

    public void createNewUserWithRabbitmq(RegisterModel model) {

        UserProfile userProfile = userMapper.toUserProfile(model);

        save(userProfile);

    }

    public UserProfile savedto(UserSaveRequestDto dto) {

        UserProfile userProfile = userMapper.toUserProfile(dto);

        return save(userProfile);

    }

    public String logindto(UserLoginDto dto) {

        if (userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).isEmpty()) {
            throw new UserManagerException(ErrorType.DOLOGIN_USERNAMEORPASSWORD_NOTEXISTS);
        } else {
            UserProfile userProfile = userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).get();
            return String.valueOf(jwtTokenManager.createToken(userProfile.getId()).get());
        }

    }

    public List<UserProfile> findalluser(UserProfile userProfile) {

        if (userProfile.getRole().equals(ERole.COMPANY_MANAGER) || userProfile.getRole().equals(ERole.ADMIN)) {
            return userRepository.findAll();
        } else throw new UserManagerException(ErrorType.NO_PERMISION);
    }

    public String delete(Long id) {

        try {
            delete(id);
            return "Başarılı";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void activation(String username) {

        if (userRepository.findByUsername(username).isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        } else {
            UserProfile userProfile = userRepository.findByUsername(username).get();
            userProfile.setStatus(EStatus.ACTIVE);
            update(userProfile);
        }
    }

    public String requestParentalLeave(UserLoginDto dto, int days) {

        if (userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).isPresent()) {
            UserProfile userProfile = userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).get();

            if (userProfile.getGender() == EGender.MALE) {
                if (days <= 10) {
                    userProfile.setParentalLeave(userProfile.getParentalLeave() + days);
                    System.out.println(userProfile.getName() + " " + days + " günlük babalık izni aldı.");
                    update(userProfile);
                } else {
                    System.out.println("Erkek çalışanlar en fazla 10 günlük babalık izni alabilir.");
                }
            } else if (userProfile.getGender() == EGender.FEMALE) {
                if (days <= 180) {
                    userProfile.setParentalLeave(userProfile.getParentalLeave() + days);
                    System.out.println(userProfile.getName() + " " + days + " günlük annelik izni aldı.");
                    update(userProfile);
                } else {
                    System.out.println("Kadın çalışanlar en fazla 6 aylık annelik izni alabilir.");
                }
            } else {
                System.out.println("Cinsiyet bilgisi hatalı.");
            }
        } else {
            throw new UserManagerException(ErrorType.DOLOGIN_USERNAMEORPASSWORD_NOTEXISTS);
        }

        return "İzin alma başarılı.";
    }

    public String requestAnnualLeave(UserLoginDto dto, int days) {

        if (userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).isPresent()) {
            UserProfile userProfile = userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).get();

            if (days <= userProfile.getTotalAnnualLeave()) {
                userProfile.setTotalAnnualLeave(userProfile.getTotalAnnualLeave() - days);
                System.out.println(userProfile.getName() + " " + days + " günlük yıllık izin aldı.");
                update(userProfile);
            } else {
                System.out.println("Yıllık izin hakkınız yetersiz.");
            }
        } else {
            throw new UserManagerException(ErrorType.DOLOGIN_USERNAMEORPASSWORD_NOTEXISTS);
        }

        return "İzin alma başarılı.";
    }

    public int getTotalAnnualLeave(UserLoginDto dto) {

        if (userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).isPresent()) {
            UserProfile userProfile = userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).get();
            return userProfile.getTotalAnnualLeave();
        } else {
            throw new UserManagerException(ErrorType.DOLOGIN_USERNAMEORPASSWORD_NOTEXISTS);
        }
    }
}