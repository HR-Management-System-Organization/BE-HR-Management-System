package com.hrproject.config.security;

import com.hrproject.repository.entity.Company;
import com.hrproject.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JwtUserDetails implements UserDetailsService {

    @Autowired
    private CompanyService companyService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByUserId(Long id) throws UsernameNotFoundException {
        Optional<Company> auth = companyService.findById(1L);


//        if (auth.isPresent()) {
//            List<GrantedAuthority> authorityList = new ArrayList<>();
//            authorityList.add(new SimpleGrantedAuthority(auth.get().getRole().toString()));
//            return User.builder()
//                    .username(auth.get().getCompanyName())
//                    .password(auth.get().getCompanyMail())
//                    .accountLocked(false)
//                    .accountExpired(false)
//                    .authorities(authorityList)
//                    .build();
//        }List<GrantedAuthority> authorityList = new ArrayList<>();
//            authorityList.add(new SimpleGrantedAuthority(auth.get().getRole().toString()));
        List<GrantedAuthority> authorityList = new ArrayList<>();
//            authorityList.add(new SimpleGrantedAuthority(auth.get().getRole().toString()));
        return User.builder()
                .username(auth.get().getCompanyName())
                .password(auth.get().getCompanyCountry())
                .accountLocked(false)
                .accountExpired(false)
                .authorities(authorityList)
                .build();
    }
}