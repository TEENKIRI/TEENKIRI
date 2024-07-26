package com.beyond.teenkiri.event.dto;

import com.beyond.teenkiri.event.domain.Event;
import com.beyond.teenkiri.user.domain.User;
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

    public Event toEntity(User user){
        return Event.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .build();
    }
}
