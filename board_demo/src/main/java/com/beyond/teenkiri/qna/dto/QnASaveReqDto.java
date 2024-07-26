package com.beyond.teenkiri.qna.dto;

import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.user.domain.User;
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