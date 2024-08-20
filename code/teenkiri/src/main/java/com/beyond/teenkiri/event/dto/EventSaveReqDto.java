package com.beyond.teenkiri.event.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.event.domain.Event;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


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
    private User user;
    private MultipartFile image;


    public Event toEntity(){
        return Event.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .delYN(this.delYN)
                .build();
    }
}
