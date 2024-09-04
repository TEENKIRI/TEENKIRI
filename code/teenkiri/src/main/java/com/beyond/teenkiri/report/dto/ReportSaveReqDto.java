package com.beyond.teenkiri.report.dto;

import com.beyond.teenkiri.chat.domain.Chat;
import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.report.domain.Reason;
import com.beyond.teenkiri.report.domain.Report;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportSaveReqDto {
    private String reportEmail;
    private String suspectEmail;
    private Reason reason;
    private Long qnaId;
    private Long postId;
    private Long commentId;
    private Long chatMessageId;
    private String details;

    @Builder.Default
    private DelYN delYN = DelYN.N;

    public Report toEntity(User user, QnA qna, Post post, Comment comment, Chat chat) {
        return Report.builder()
                .user(user)
                .reason(this.reason)
                .qna(qna)
                .post(post)
                .delYN(this.delYN)
                .comment(comment)
                .chat(chat)
                .details(this.details)
                .build();
    }
}
