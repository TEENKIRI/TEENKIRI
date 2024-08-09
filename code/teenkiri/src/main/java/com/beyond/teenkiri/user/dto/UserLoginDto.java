package com.beyond.teenkiri.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDto {
    private String email;
    private String password;
}
