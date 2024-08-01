package com.beyond.teenkiri.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserFindPasswordDto {
    private String username;
    private String phone;
    private String email;
}
