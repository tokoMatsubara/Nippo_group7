package com.daily_app.demo.config;


import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.daily_app.demo.Service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterchain)
        throws ServletException, IOException{
        Cookie cookies[] = request.getCookies();
        if(cookies != null && cookies.length > 0){
            System.out.println("cookieがnullじゃない！");
            String email = Arrays.stream(cookies)
                .filter(c -> "token".equals(c.getName()))
                .map(c -> c.getValue())
                .findFirst()
                .orElse(null);
            
            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(email);
                
            UsernamePasswordAuthenticationToken token = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        System.out.println("カスタムフィルター終了");

        filterchain.doFilter(request, response);
    }
}
