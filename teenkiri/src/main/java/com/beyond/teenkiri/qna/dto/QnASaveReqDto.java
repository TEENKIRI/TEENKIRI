package com.beyond.teenkiri.qna.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.user_board.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnASaveReqDto {
    private String userEmail;
    private String title;
    private String questionText;
    private LocalDateTime createdTime;
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public QnA toEntity(User user) {
        return QnA.builder()
                .user(user)
                .title(this.title)
                .questionText(this.questionText)
                .build();
    }
}