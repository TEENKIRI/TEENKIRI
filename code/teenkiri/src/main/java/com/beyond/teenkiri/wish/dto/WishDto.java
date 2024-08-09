package com.beyond.teenkiri.wish.dto;

import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.wish.domain.Wish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishDto {
    private Long id;
    private Long userId;
    private Long subjectId;

    public Wish toEntity(User user, Subject subject) {
        return Wish.builder()
                .id(this.id)
                .user(user)
                .subject(subject)
                .build();
    }

    public static WishDto fromEntity(Wish wish) {
        return WishDto.builder()
                .id(wish.getId())
                .userId(wish.getUser().getId())
                .subjectId(wish.getSubject().getId())
                .build();
    }
}
