package com.daily_app.demo.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.daily_app.demo.Entity.User;

public class CustomUserDetails implements UserDetails {
    User user;

    public CustomUserDetails(User user){
        this.user = user;
    }

    public Integer getId(){
        return user.getUserId();
    }

    public User getUser(){
        return user;
    }

    @Override
    public String getUsername(){
        return user.getMailAddress();
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // "ROLE_" プレフィックスは hasRole(...) で必要になる慣習
        return List.of(new SimpleGrantedAuthority("ROLE_User"));
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
