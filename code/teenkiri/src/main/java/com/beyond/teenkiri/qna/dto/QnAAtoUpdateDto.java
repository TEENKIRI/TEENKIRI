package com.beyond.teenkiri.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnAAtoUpdateDto {
    private String answererEmail;
    private String answerText;
    private LocalDateTime answeredAt;
    private MultipartFile aImage;
}