package com.beyond.teenkiri.post.dto;


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
public class PostUpdateDto {
    private String userEmail;
    private String title;
    private String content;
    private MultipartFile image;
}
