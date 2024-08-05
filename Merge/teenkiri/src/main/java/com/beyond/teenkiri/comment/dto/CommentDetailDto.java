package com.beyond.teenkiri.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDetailDto {
    private Long id;
    private String userEmail;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Long QnAId;
    private Long postId;


}
