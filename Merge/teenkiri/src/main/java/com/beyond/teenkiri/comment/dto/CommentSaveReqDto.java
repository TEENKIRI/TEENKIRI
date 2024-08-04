package com.beyond.teenkiri.comment.dto;

import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.qna.domain.QnA;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentSaveReqDto {
    private Long postId;
    private Long qnaId;
    private String userEmail;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    @Builder.Default
    private DelYN delYn = DelYN.N;


    public Comment PostToEntity(com.beyond.teenkiri.user_board.domain.user user, Post post){
        return Comment.builder()
                .content(this.content)
                .delYN(this.delYn)
                .createdTime(this.createdTime)
                .user(user)
                .post(post)
                .build();
    }

    public Comment QnAToEntity(com.beyond.teenkiri.user_board.domain.user user, QnA qnA){
        return Comment.builder()
                .content(this.content)
                .delYN(this.delYn)
                .createdTime(this.createdTime)
                .user(user)
                .qna(qnA)
                .build();
    }
}
