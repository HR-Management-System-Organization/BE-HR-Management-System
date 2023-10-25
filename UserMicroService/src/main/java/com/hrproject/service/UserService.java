package com.hrproject.service;

import com.hrproject.dto.request.UserLoginDto;
import com.hrproject.dto.request.UserSaveRequestDto;
import com.hrproject.exception.ErrorType;
import com.hrproject.exception.UserManagerException;
import com.hrproject.mapper.IUserMapper;
import com.hrproject.repository.IUserRepository;
import com.hrproject.repository.entity.UserProfile;
import com.hrproject.repository.enums.EStatus;
import com.hrproject.utility.JwtTokenManager;
import com.hrproject.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceManager<UserProfile,Long> { //extends ServiceManager<UserProfile, String> {

    private final IUserRepository userRepository;

    private final JwtTokenManager jwtTokenManager;

    private final IUserMapper userMapper;


    public UserService(IUserRepository userRepository, JwtTokenManager jwtTokenManager, IUserMapper userMapper) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.userMapper = userMapper;
    }

    public UserProfile savedto(UserSaveRequestDto dto){

        UserProfile userProfile=userMapper.toUserProfile(dto);
        return save(userProfile);

    }

    public String logindto(UserLoginDto dto){
        if (userRepository.findOptionalByUsernameAndPassword(dto.getUsername(),dto.getPassword()).isEmpty()){
            throw new UserManagerException(ErrorType.DOLOGIN_USERNAMEORPASSWORD_NOTEXISTS);
        }
        else {
            UserProfile userProfile=userRepository.findOptionalByUsernameAndPassword(dto.getUsername(),dto.getPassword()).get();
            return String.valueOf(jwtTokenManager.createToken(userProfile.getId()).get());
        }


    }
    public String activation(String tokken){
        if (jwtTokenManager.verifyToken(tokken)){
            if (jwtTokenManager.verifyactivationcode(tokken).get()){
                if (userRepository.findById(jwtTokenManager.getAuthIdFromToken(tokken).get()).isPresent()){

                    UserProfile userProfile=userRepository.findById(jwtTokenManager.getAuthIdFromToken(tokken).get()).get();
                    if (jwtTokenManager.verifyactivationcode(tokken).get()){
                        userProfile.setStatus(EStatus.ACTIVE);
                        userRepository.save(userProfile);
                        return "succesfull";
                    }

                }
            }
        }
        return "unsuccesfull";
    }
}