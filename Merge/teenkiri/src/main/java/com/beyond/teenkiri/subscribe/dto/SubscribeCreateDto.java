package com.beyond.teenkiri.subscribe.dto;

import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subscribe.domain.Subscribe;
import com.beyond.teenkiri.subscribe.domain.WishType;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeCreateDto {
    private String userEmail;
    private String subjectTitle;
    @Builder.Default
    private WishType wishType = WishType.WISH;

    public Subscribe toEntity(User user, Subject subject){
        return Subscribe.builder()
                .user(user)
                .subject(subject)
                .wishType(this.wishType)
                .build();

    }
}
