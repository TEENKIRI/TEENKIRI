//package com.beyond.teenkiri.report.domain;
//
//import com.beyond.teenkiri.comment.domain.Comment;
//import com.beyond.teenkiri.common.domain.BaseTimeEntity;
//import com.beyond.teenkiri.common.domain.DelYN;
//import com.beyond.teenkiri.qna.domain.QnA;
//import com.beyond.teenkiri.post.domain.Post;
//import com.beyond.teenkiri.report.dto.ReportListResDto;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Entity
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class Report extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    private DelYN delYN;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Reason reason;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private com.beyond.teenkiri.user_board.domain.user user;
//
//    @ManyToOne
//    @JoinColumn(name = "qna_id", nullable = true)
//    private QnA qna;
//
//    @ManyToOne
//    @JoinColumn(name = "post_id", nullable = true)
//    private Post post;
//
//    @ManyToOne
//    @JoinColumn(name = "comment_id", nullable = true)
//    private Comment comment;
//
////    ALTER TABLE report MODIFY COLUMN qna_id BIGINT NULL;
////    ALTER TABLE report MODIFY COLUMN post_id BIGINT NULL;
////    데이터 베이스 수정을 통해서 null값이 가능하도록 한다.
//    public ReportListResDto listFromEntity() {
//        return ReportListResDto.builder()
//                .id(this.id)
//                .reportEmail(this.user.getEmail())
//                .suspectEmail(this.qna != null ? this.qna.getUser().getEmail() : this.post.getUser().getEmail())
//                .reason(this.reason)
//                .qnaId(this.qna != null ? this.qna.getId() : null )
//                .postId(this.post != null ? this.post.getId() : null)
//                .createdTime(this.getCreatedTime())
//                .build();
//    }
//}
package com.beyond.teenkiri.report.domain;

import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.report.dto.ReportListResDto;
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
    private DelYN delYN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Reason reason;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private com.beyond.teenkiri.user_board.domain.user user;

    @ManyToOne
    @JoinColumn(name = "qna_id", nullable = true)
    private QnA qna;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;

    public ReportListResDto listFromEntity() {
        return ReportListResDto.builder()
                .id(this.id)
                .reportEmail(this.user.getEmail())
                .suspectEmail(determineSuspectEmail())
                .reason(this.reason)
                .qnaId(this.qna != null ? this.qna.getId() : null)
                .postId(this.post != null ? this.post.getId() : null)
                .commentId(this.comment != null ? this.comment.getId() : null)
                .createdTime(this.getCreatedTime())
                .build();
    }

    private String determineSuspectEmail() {
        if (this.qna != null) {
            return this.qna.getUser().getEmail();
        } else if (this.post != null) {
            return this.post.getUser().getEmail();
        } else if (this.comment != null) {
            return this.comment.getUser().getEmail();
        } else {
            return null;
        }
    }
}
