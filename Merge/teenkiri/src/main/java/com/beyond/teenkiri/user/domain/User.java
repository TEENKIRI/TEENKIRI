package com.beyond.teenkiri.user.domain;

import com.beyond.teenkiri.common.domain.DelYN;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserType userType = UserType.STUDENT;

    @Column(nullable = false)
    private boolean isVerified = false;

    private int reportCount = 0;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public User(String username, String password, String phone, String address, String email, String nickname, UserType userType, boolean isVerified, int reportCount, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, DelYN delYn) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.nickname = nickname;
        this.userType = userType;
        this.isVerified = isVerified;
        this.reportCount = reportCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.delYN = delYN;
    }

    public enum UserType {
        STUDENT, TEACHER, ADMIN
    }

}
