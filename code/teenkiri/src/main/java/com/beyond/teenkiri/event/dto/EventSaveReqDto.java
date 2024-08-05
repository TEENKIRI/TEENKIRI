package com.beyond.teenkiri.event.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.event.domain.Event;
import com.beyond.teenkiri.user_board.domain.user;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventSaveReqDto {
    private String title;
    private String content;
    private String userEmail;
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public Event toEntity(user user){
        return Event.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .delYN(this.delYN)
                .build();
    }
}
