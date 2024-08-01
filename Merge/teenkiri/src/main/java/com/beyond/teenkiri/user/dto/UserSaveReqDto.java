package com.beyond.teenkiri.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSaveReqDto {
    private String username;
    private String email;
    private String password;
    private String nickname;
    private String address;
    private String phone;
}
