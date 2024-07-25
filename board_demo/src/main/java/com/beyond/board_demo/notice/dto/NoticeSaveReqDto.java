package com.beyond.board_demo.notice.dto;

import com.beyond.board_demo.notice.domain.Notice;
import com.beyond.board_demo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeSaveReqDto {
    private String title;
    private String content;
    private String userEmail;

    public Notice toEntity(User user) {
        return Notice.builder()
                .user(user)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
