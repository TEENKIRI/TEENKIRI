package com.beyond.teenkiri.comment.dto;

import com.beyond.teenkiri.common.domain.DelYN;
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
    @Builder.Default
    private DelYN delYN = DelYN.N;

}
