package com.beyond.board_demo.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnAAnswerReqDto {
    private String answererEmail;
    private String answerText;
}
