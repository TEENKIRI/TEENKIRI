package com.beyond.teenkiri.user.config;

import com.beyond.teenkiri.user.Handler.OAuth2SuccessHandler;
import com.beyond.teenkiri.user.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenprovider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Autowired
    public SecurityConfig(JwtTokenprovider jwtTokenProvider,
                          UserDetailsService userDetailsService,
                          CustomOAuth2UserService customOAuth2UserService,
                          OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 보호 비활성화
                .cors().and() // CORS 활성화
                .authorizeRequests()
                .antMatchers("**").permitAll() // 모든 요청을 허용
                .anyRequest().authenticated() // 나머지 요청은 인증된 사용자만 허용
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService) // OAuth2 사용자 서비스 설정
                .and()
                .successHandler(oAuth2SuccessHandler) // OAuth2 로그인 성공 핸들러 설정
                .failureUrl("/login?error=true") // 로그인 실패 시 리다이렉션할 URL
                .and()
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가
    }
}
