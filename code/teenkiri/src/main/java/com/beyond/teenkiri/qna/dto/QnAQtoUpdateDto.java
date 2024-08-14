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
public class QnAQtoUpdateDto {
    private String userEmail;
    private String questionText;
    private String title;
    private LocalDateTime updatedTime;
    private MultipartFile qImage;
}