package com.beyond.board_demo.qna.dto;

import com.beyond.board_demo.qna.domain.QnA;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnADetailDto {
    private Long id;
    private String title;
    private String questionText;
    private String answerText;
    private String questionUserNickname;
    private String answeredByNickname;
    private LocalDateTime createdTime;
    private LocalDateTime answeredAt;

    public static QnADetailDto fromEntity(QnA qna) {
        return QnADetailDto.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .questionText(qna.getQuestionText())
                .answerText(qna.getAnswerText())
                .questionUserNickname(qna.getUser().getNickname())
                .answeredByNickname(qna.getAnsweredBy() != null ? qna.getAnsweredBy().getNickname() : null)
                .createdTime(qna.getCreatedTime())
                .answeredAt(qna.getAnsweredAt())
                .build();
    }
}
