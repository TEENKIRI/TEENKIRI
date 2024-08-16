package com.beyond.teenkiri.comment.dto;

import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.user.domain.User;
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
    private Long userId;  // userEmail 대신 userId로 변경
    private String content;
    private Long userEmail;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    @Builder.Default
    private DelYN delYn = DelYN.N;

    // toEntity 메서드 수정: 닉네임을 포함하도록 변경
    public Comment PostToEntity(User user, Post post, String nickname){
        return Comment.builder()
                .content(this.content)
                .delYN(this.delYn)
                .createdTime(this.createdTime)
                .user(user)
                .nickname(nickname)  // 닉네임 설정
                .post(post)
                .build();
    }

    public Comment QnAToEntity(User user, QnA qnA, String nickname){
        return Comment.builder()
                .content(this.content)
                .delYN(this.delYn)
                .createdTime(this.createdTime)
                .user(user)
                .nickname(nickname)  // 닉네임 설정
                .qna(qnA)
                .build();
    }
}
