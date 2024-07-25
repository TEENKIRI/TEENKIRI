package com.beyond.board_demo.user.domain;

import com.beyond.board_demo.common.BaseTimeEntity;
import com.beyond.board_demo.common.DelYN;
import com.beyond.board_demo.post.domain.Post;
import com.beyond.board_demo.user.dto.UserDetailDto;
import com.beyond.board_demo.user.dto.UserListResDto;
import com.beyond.board_demo.user.dto.UserUpdateDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 15)
    private String nickname;

    @Column(unique = true, nullable = false, length = 30)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 20, nullable = false, unique = true)
    private String phone;

    @Column(length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('N', 'Y') DEFAULT 'N'")
    private DelYN delYN;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    public UserListResDto listFromEntity() {
        return UserListResDto.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .address(this.address)
                .phone(this.phone)
                .nickname(this.nickname)
                .build();
    }

    public void toUpdate(UserUpdateDto dto) {
        this.name = dto.getName();
        this.nickname = dto.getNickname();
        this.password = dto.getPassword();
        this.address = dto.getAddress();
        this.phone = dto.getPhone();
    }

    public UserDetailDto detailFromEntity() {
        return UserDetailDto.builder()
                .id(this.getId())
                .email(this.getEmail())
                .name(this.getName())
                .nickname(this.nickname)
                .password(this.getPassword())
                .role(this.getRole())
                .createdTime(this.getCreatedTime())
                .postCounts((long) this.getPosts().size())
                .address(this.getAddress())
                .phone(this.getPhone())
                .build();
    }
}
