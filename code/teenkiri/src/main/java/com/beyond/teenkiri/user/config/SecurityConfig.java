package com.beyond.teenkiri.user.config;

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

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final JwtTokenprovider jwtTokenProvider;
//    private final UserDetailsService userDetailsService;
//
//    @Autowired
//    public SecurityConfig(JwtTokenprovider jwtTokenProvider, UserDetailsService userDetailsService) {
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .cors().and()
//                .authorizeRequests()
//
//                .antMatchers("**").permitAll()
////                .antMatchers("/api/login", "/api/register").permitAll() // adjust this according to your endpoints
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class);
//    }
//}
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenprovider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(JwtTokenprovider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/", "/oauth2/**", "/login/**", "/css/**", "/js/**", "/img/**").permitAll() // 구글 로그인 경로를 포함한 공용 리소스 허용
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/") // 로그인 성공 후 리디렉션할 URL
                .failureUrl("/login?error=true") // 로그인 실패 시 리디렉션할 URL
                .and()
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class);
    }
}
