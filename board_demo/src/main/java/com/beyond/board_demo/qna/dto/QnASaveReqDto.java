package com.beyond.board_demo.qna.dto;

import com.beyond.board_demo.qna.domain.QnA;
import com.beyond.board_demo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnASaveReqDto {
    private String userEmail;
    private String title;
    private String questionText;

    public QnA toEntity(User user) {
        return QnA.builder()
                .user(user)
                .title(this.title)
                .questionText(this.questionText)
                .build();
    }
}