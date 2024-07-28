package com.beyond.teenkiri.User.dto;

import com.beyond.teenkiri.User.domain.DelYn;
import com.beyond.teenkiri.User.domain.User;
import com.beyond.teenkiri.User.domain.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String phone;

    private String address;

    @NotNull
    private String email;

    @NotNull
    private String nickname;

    @Builder.Default
    private UserType userType = UserType.STUDENT;

    @Builder.Default
    private Boolean isVerified = false;

    @Builder.Default
    private Integer reportCount = 0;

    private LocalDateTime deletedAt;

    @Builder.Default
    private DelYn delYn = DelYn.N;

    public UserDto withEncodedPassword(String encodedPassword) {
        return UserDto.builder()
                .username(this.username)
                .password(encodedPassword)
                .phone(this.phone)
                .address(this.address)
                .email(this.email)
                .nickname(this.nickname)
                .userType(this.userType)
                .isVerified(this.isVerified)
                .reportCount(this.reportCount)
                .deletedAt(this.deletedAt)
                .delYn(this.delYn)
                .build();
    }

    public static UserDto toEntity(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .phone(user.getPhone())
                .address(user.getAddress())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userType(user.getUserType())
                .isVerified(user.getIsVerified())
                .reportCount(user.getReportCount())
                .deletedAt(user.getDeletedAt())
                .delYn(user.getDelYn())
                .build();
    }

}