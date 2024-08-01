package com.beyond.teenkiri.user.sevice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserDetailServiceConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // InMemoryUserDetailsManager는 테스트 목적으로 사용되었지만,
        // 실제 프로젝트에서는 JPA를 사용하여 DB에서 사용자 정보를 로드하도록 구현해야 합니다.
        return new InMemoryUserDetailsManager();
    }
}