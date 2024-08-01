package com.beyond.teenkiri.User.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserFindIdDto {
    private String username;
    private String phone;
}
