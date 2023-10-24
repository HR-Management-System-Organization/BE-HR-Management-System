package com.hrproject.service;

import com.hrproject.dto.request.AuthUpdateRequestDto;
import com.hrproject.dto.request.UserProfileUpdateRequestDto;
import com.hrproject.dto.request.UserSaveRequestDto;
import com.hrproject.dto.response.UserProfileFindAllResponseDto;
import com.hrproject.dto.response.UserProfileResponseDto;
import com.hrproject.exception.ErrorType;
import com.hrproject.exception.UserManagerException;
import com.hrproject.manager.IAuthManager;
import com.hrproject.mapper.IUserMapper;
import com.hrproject.rabbitmq.model.RegisterModel;
import com.hrproject.rabbitmq.producer.RegisterElasticProducer;
import com.hrproject.repository.IUserRepository;
import com.hrproject.repository.entity.UserProfile;
import com.hrproject.repository.enums.EStatus;
import com.hrproject.utility.JwtTokenManager;
import com.hrproject.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService { //extends ServiceManager<UserProfile, String> {


}