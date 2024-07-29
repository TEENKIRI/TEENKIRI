package com.beyond.teenkiri.common;

import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.dto.UserSaveReqDto;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.beyond.teenkiri.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner { // 프로그램 시작 되자마자 실행되는 comandline
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
         if (userRepository.findByEmail("admin@test.com") != null){
             userService.userCreate(UserSaveReqDto.builder()
                     .name("admin")
                     .nickname("admin")
                     .email("admin@test.com")
                     .phone("0000000000")
                     .password("12341234")
                     .role(Role.ADMIN)
                     .build());
         }

    }

}
