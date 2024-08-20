package com.beyond.teenkiri.user.dto;

import com.beyond.teenkiri.common.domain.Address;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import lombok.*;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveReqDto {
    private String name;
    private String email;
    private String password;
    @Builder.Default
    private String nickname = String.valueOf(UUID.randomUUID());
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
                .email(this.email)
                .nickname(this.nickname != null ? this.nickname : String.valueOf(UUID.randomUUID())) // nickname이 null이면 UUID 생성
                .role(this.role)
                .address(this.address)
                .delYN(this.delYN)
                .phone(this.phone)
                .build();
    }
    public NicknameCheckDto toNicknameCheckDto() {
        return new NicknameCheckDto(this.nickname);
    }
}
