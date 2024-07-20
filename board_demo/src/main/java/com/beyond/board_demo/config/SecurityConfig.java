//package com.beyond.board_demo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests
//                                .antMatchers("/notice/create").hasRole("ADMIN")
//                                .anyRequest().authenticated()
//                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/login")
//                                .permitAll()
//                )
//                .logout(logout ->
//                        logout
//                                .logoutSuccessUrl("/")
//                );
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(org.springframework.security.core.userdetails.User
//                .withUsername("admin")
//                .password("{noop}password") // {noop} is used to indicate no password encoder
//                .roles("ADMIN")
//                .build());
//        manager.createUser(org.springframework.security.core.userdetails.User
//                .withUsername("user")
//                .password("{noop}password")
//                .roles("USER")
//                .build());
//        return manager;
//    }
//}
