package com.beyond.teenkiri.report.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.report.dto.ReportDetailDto;
import com.beyond.teenkiri.report.dto.ReportListResDto;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DelYN delYN = DelYN.N;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Reason reason;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "qna_id", nullable = false)
    private QnA qna;

    public ReportListResDto fromListEntity() {
        return ReportListResDto.builder()
                .reportEmail(this.user.getEmail())
                .suspectEmail(this.user.getEmail())
                .reason(this.reason)
                .qnaId(this.qna.getId())
                .build();
    }

    public ReportDetailDto fromDetailEntity() {
        return ReportDetailDto.builder()
                .id(this.id)
                .reportEmail(this.user.getEmail())
                .suspectEmail(this.user.getEmail())
                .reason(this.reason)
                .qnaId(this.qna.getId())
                .build();
    }


}
