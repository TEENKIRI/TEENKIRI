package com.beyond.board_demo.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserListResDto {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String address;
    private String phone;

}
