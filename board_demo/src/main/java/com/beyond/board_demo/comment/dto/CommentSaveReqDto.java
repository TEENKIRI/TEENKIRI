package com.beyond.board_demo.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentSaveReqDto {
    private Long postId;
    private Long qnaId;
    private String userEmail;
    private String content;
}
