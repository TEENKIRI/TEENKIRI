package com.beyond.teenkiri.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateReqDto {
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private String address;
}
