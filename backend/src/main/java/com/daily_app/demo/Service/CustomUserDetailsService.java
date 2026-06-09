package com.daily_app.demo.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Repository.UserRepository;

// ユーザー認証に用いる専用のUserDetailsを作る
@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        User user = userRepository.findByMailAddress(email).orElseThrow(() -> 
            new UsernameNotFoundException("User not found" + email)
        );

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUserName())
            .password(user.getPassword())
            .authorities("ROLE_USER")
            .build();
    }
}
