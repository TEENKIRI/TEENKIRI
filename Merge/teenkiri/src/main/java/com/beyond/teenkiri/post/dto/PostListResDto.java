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
    private String user_email;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
