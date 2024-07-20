package com.beyond.board_demo.comment.dto;

import com.beyond.board_demo.comment.domain.Comment;
import com.beyond.board_demo.notice.domain.Notice;
import com.beyond.board_demo.post.domain.Post;
import com.beyond.board_demo.qna.domain.QnA;
import com.beyond.board_demo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentSaveReqDto {
    private String content;
    private String userEmail;

    public Comment toEntity(User user, Post post, QnA qna, Notice notice) {
        return Comment.builder()
                .content(this.content)
                .user(user)
                .post(post)
                .qna(qna)
                .notice(notice)
                .build();
    }
}
