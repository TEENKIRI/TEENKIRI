package com.beyond.teenkiri.user.dto;

import com.beyond.teenkiri.common.domain.Address;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserSaveReqDto {
    private String name;
    private String email;
    private String password;
    private String nickname;
    private Address address;
    private String phone;
    @Builder.Default
    private Role role = Role.STUDENT;
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public User toEntity(String password){
        return User.builder()
                .password(password)
                .name(this.name)
                .nickname(this.nickname)
                .email(this.email)
                .role(this.role)
                .address(this.address)
                .delYN(this.delYN)
                .phone(this.phone)
                .build();
    }
}
