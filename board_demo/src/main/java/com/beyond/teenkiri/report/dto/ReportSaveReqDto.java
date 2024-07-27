package com.beyond.teenkiri.report.dto;

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

    public Report toEntity(User user, QnA qna, Post post) {
        return Report.builder()
                .user(user)
                .reason(this.reason)
                .qna(qna)
                .post(post)
                .build();
    }
}
