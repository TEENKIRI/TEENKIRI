package com.beyond.teenkiri.qna.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnAQtoUpdateDto {
    private String userEmail;
    private String questionText;
    private String title;
}
