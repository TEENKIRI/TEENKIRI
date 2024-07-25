package com.beyond.board_demo.user.dto;


import com.beyond.board_demo.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailDto {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String password;
    private LocalDateTime createdTime;
    private Role role;
    private Long postCounts;
    private String address;
    private String phone;
}
