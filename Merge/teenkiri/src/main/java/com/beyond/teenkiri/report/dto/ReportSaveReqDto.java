package com.beyond.teenkiri.report.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.report.domain.Reason;
import com.beyond.teenkiri.report.domain.Report;
import com.beyond.teenkiri.user_board.domain.user;
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

    @Builder.Default
    private DelYN delYN = DelYN.N;

    public Report toEntity(user user, QnA qna, Post post) {
        return Report.builder()
                .user(user)
                .reason(this.reason)
                .qna(qna)
                .post(post)
                .build();
    }
}
