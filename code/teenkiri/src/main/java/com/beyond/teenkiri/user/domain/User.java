package com.beyond.teenkiri.user.domain;

import com.beyond.teenkiri.common.domain.Address;
import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.user.dto.UserListDto;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @Column(length = 255)
    private Address address;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.STUDENT;

    @Column(nullable = false)
    private boolean isVerified = false;

    private int reportCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;


    public UserListDto listFromEntity(){
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
}