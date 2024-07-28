package com.beyond.teenkiri.User.domain;

import com.beyond.teenkiri.User.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column
    private String address;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserType userType = UserType.STUDENT;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isVerified = false;

    @Column
    @Builder.Default
    private Integer reportCount = 0;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DelYn delYn = DelYn.N;

    @Column
    private String resetToken;

    public static User fromEntity(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .phone(userDto.getPhone())
                .address(userDto.getAddress())
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .userType(userDto.getUserType() != null ? userDto.getUserType() : UserType.STUDENT)
                .isVerified(userDto.getIsVerified() != null ? userDto.getIsVerified() : false)
                .reportCount(userDto.getReportCount() != null ? userDto.getReportCount() : 0)
                .deletedAt(userDto.getDeletedAt())
                .delYn(userDto.getDelYn() != null ? userDto.getDelYn() : DelYn.N)
                .build();
    }
}
