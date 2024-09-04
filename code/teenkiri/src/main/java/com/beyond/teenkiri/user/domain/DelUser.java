package com.beyond.teenkiri.user.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DelUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    public static DelUser toEntity(String email) {
        return DelUser.builder()
                .email(email)
                .build();
    }
}
