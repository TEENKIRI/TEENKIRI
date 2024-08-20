package com.beyond.teenkiri.user.domain;

import com.beyond.teenkiri.common.domain.Address;
import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.user.dto.UserListDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column
    private String password;

    @Column(length = 20, unique = true)
    private String phone;

    @Column(length = 255)
    private Address address;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 100, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.STUDENT;

    //    @Column()
    private boolean isVerified = false;

    @Builder.Default
    private int reportCount = 0;

    // provider : google이 들어감
    private String provider;

    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;


    public UserListDto listFromEntity() {
        return UserListDto.builder()
                .id(this.id)
                .name(this.name)
                .phone(this.phone)
                .address(this.address)
                .email(this.email)
                .nickname(this.nickname)
                .role(this.getRole())
                .reportCount(this.reportCount)
                .delYN(this.delYN)
                .build();
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public User update(String name) {
        this.name = name;
        return this;
    }

    @Builder
    public User(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.nickname = String.valueOf(UUID.randomUUID());
    }

    public User updateNick(String uuid) {
        this.nickname = uuid;
        return this;
    }

    public User updatePass(String uuid) {
        this.password = uuid;
        return this;
    }

    public User updateAddress(String temp) {
        this.address = new Address(temp, temp, temp);
        return this;
    }
}