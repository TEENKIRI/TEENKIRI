package com.beyond.teenkiri.user.dto;

import com.beyond.teenkiri.common.domain.Address;
import com.beyond.teenkiri.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEditReqDto {
    private String nickname;
    private String password;
    private String confirmPassword;
    private Address address;

    @Builder
    public UserEditReqDto(String nickname, String password, String confirmPassword, Address address) {
        this.nickname = nickname;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.address = address;
    }

    public User toEntity() {
        return User.builder()
                .nickname(this.nickname)
                .password(this.password)
                .address(this.address)
                .build();
    }
}
