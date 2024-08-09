package com.beyond.teenkiri.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDto {
    private Long id;
    private String title;
    private String contents;
    private String nickname;
    private String imageUrl;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
