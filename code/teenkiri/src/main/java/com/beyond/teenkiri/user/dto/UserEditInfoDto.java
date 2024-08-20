package com.beyond.teenkiri.user.dto;

import com.beyond.teenkiri.common.domain.Address;
import com.beyond.teenkiri.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEditInfoDto {
    private String nickname;
    private String name;
    private String email;
    private String phone;
    private Address address;

    @Builder
    public UserEditInfoDto(String nickname, String name, String email, String phone, Address address) {
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public static UserEditInfoDto fromEntity(User user) {
        return UserEditInfoDto.builder()
                .nickname(user.getNickname())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();
    }
}
