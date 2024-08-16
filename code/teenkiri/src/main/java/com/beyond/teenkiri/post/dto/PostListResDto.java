package com.beyond.teenkiri.post.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostListResDto {
    private Long id;
    private String title;
    private String nickname;
    private String imageUrl;
    private Long user_id;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
