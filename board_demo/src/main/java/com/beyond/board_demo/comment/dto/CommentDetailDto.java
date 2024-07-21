package com.beyond.board_demo.comment.dto;

import com.beyond.board_demo.comment.domain.Comment;
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
    private Long postId;

    public static CommentDetailDto fromEntity(Comment comment) {
        return CommentDetailDto.builder()
                .id(comment.getId())
                .userEmail(comment.getUser().getEmail())
                .content(comment.getContent())
                .createdTime(comment.getCreatedTime())
                .postId(comment.getPost().getId())
                .build();
    }
}
