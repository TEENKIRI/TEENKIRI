package com.beyond.teenkiri.notice.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.notice.domain.Notice;
import com.beyond.teenkiri.user_board.domain.user;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeSaveReqDto {
    private String title;
    private String content;
    private String userEmail;
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public Notice toEntity(user user){
        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .build();
    }
}
