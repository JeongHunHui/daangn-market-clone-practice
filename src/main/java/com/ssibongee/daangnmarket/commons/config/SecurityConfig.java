package com.ssibongee.daangnmarket.commons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// 패스워드 단방향 암호화를 위해 PasswordEncoder를 빈으로 등록
@Configuration
public class SecurityConfig {

    @Value("${security.bcrypt.strength}")
    private int strength;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(strength);
    }
}
