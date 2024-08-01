package com.beyond.teenkiri.common.service;

import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.dto.UserSaveReqDto;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.beyond.teenkiri.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


// CommandLineRunner 상속함으로써 해당 컴포넌트가 스프링빈으로 등록되는 시점에 run 메서드 실행
@Component
public class InitialDataLoader implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@test.com").isEmpty()) {
            userService.userCreate(UserSaveReqDto.builder()
                    .name("admin")
                    .nickname("adminNick")
                    .email("admin@test.com")
                    .password("12341234")
                    .phone("01011112222")
                    .role(Role.ADMIN)
                    .build());
        }
    }
}
