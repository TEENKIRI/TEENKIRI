package com.beyond.teenkiri.qna.dto;

import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnAAnswerReqDto {
    private String answererEmail;
    private String answerText;

    public QnA toEntity(User answeredBy, QnA existingQnA) {
        return QnA.builder()
                .id(existingQnA.getId())
                .title(existingQnA.getTitle())
                .questionText(existingQnA.getQuestionText())
                .answerText(this.answerText)
                .user(existingQnA.getUser())
                .answeredBy(answeredBy)
                .answeredAt(existingQnA.getAnsweredAt() != null ? existingQnA.getAnsweredAt() : LocalDateTime.now())
                .delYN(existingQnA.getDelYN())
                .comments(existingQnA.getComments())
                .build();
    }
}
