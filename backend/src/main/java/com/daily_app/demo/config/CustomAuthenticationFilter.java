package com.daily_app.demo.config;


import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.daily_app.demo.Service.CustomUserDetailsService;

import io.jsonwebtoken.JwtException;
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
    private JwtTokenProvider tokenProvider;

    public CustomAuthenticationFilter(CustomUserDetailsService userDetailsService, JwtTokenProvider tokenProvider){
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterchain)
        throws ServletException, IOException{


        String token = resolveToken(request);    
        // トークンが無い / 空ならここで認証処理せず次のフィルターへ
        if (StringUtils.hasText(token)) {
            try {
                String mailAddress = tokenProvider.getMailAddress(token);
                // ... SecurityContext に Authentication をセット
                CustomUserDetails userDetails = userDetailsService.loadUserByUsername(mailAddress);
            
                UsernamePasswordAuthenticationToken userToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                SecurityContextHolder.getContext().setAuthentication(userToken);
            } catch (JwtException | IllegalArgumentException e) {
                // 無効・期限切れトークンはログだけ残して未認証扱いにする
                System.err.println("Invalid JWT: " + e.getMessage());
            }
        }
        

        filterchain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
    if (request.getCookies() == null) {
        return null;
    }
    return Arrays.stream(request.getCookies())
            .filter(c -> "accessToken".equals(c.getName()))
            .map(Cookie::getValue)
            .filter(StringUtils::hasText)
            .findFirst()
            .orElse(null);
}
}
