package com.daily_app.demo.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Repository.UserRepository;
import com.daily_app.demo.config.CustomUserDetails;

// ユーザー認証に用いる専用のUserDetailsを作る
@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email){
        User user = userRepository.findByMailAddress(email).orElseThrow(() -> 
            new UsernameNotFoundException("User not found" + email)
        );

        return new CustomUserDetails(user);
    }
}
