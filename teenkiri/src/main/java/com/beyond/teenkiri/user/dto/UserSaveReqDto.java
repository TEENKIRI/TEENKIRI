package com.beyond.teenkiri.user.dto;

import com.beyond.teenkiri.common.DelYN;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveReqDto {
    private String name;
    private String nickname;
    private String email;
    private String password;

    // 사용자가 String 요청해도 Role 클래스 자동 형변환
    @Builder.Default
    private Role role = Role.STUDENT;
    private String phone;
    private String address;
    private DelYN delYN;

    // DTO -> Entity 변환
    public User toEntity() {
        return User.builder()
                .password(this.password)
                .name(this.name)
                .nickname(this.nickname)
                .email(this.email)
                .role(this.role)
//                .posts(new ArrayList<>())//
                .address(this.address)
                .delYN(this.delYN)
                .phone(this.phone)
                .build();
    }

}
