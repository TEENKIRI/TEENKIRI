package com.beyond.teenkiri.notification.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.qna.domain.QnA;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NotificationDto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "qna_id")
//    private QnA qna;
//
//    @ManyToOne
//    @JoinColumn(name = "post_id")
//    private Post post;

    private Long qnaId;

    private Long postId;

    private String message;
    private String userEmail;

    @Enumerated(EnumType.STRING)
    private DelYN delYN;

//    public NotificationDto( QnA qna, Post post, String userEmail, String message) {
//        this.qna = qna;
//        this.post = post;
//        this.message = message;
//        this.userEmail = userEmail;
//        this.delYN = DelYN.N;
//    }
public NotificationDto( Long qnaId, Long postId, String userEmail, String message) {
    this.qnaId = qnaId;
    this.postId = postId;
    this.message = message;
    this.userEmail = userEmail;
    this.delYN = DelYN.N;
}

//    public NotificationListDto listFromEntity() {
//        return NotificationListDto.builder()
//                .id(this.id)
//                .qnaId(this.qna != null ? this.qna.getId() : null)
//                .postId(this.post != null ? this.post.getId() : null)
//                .message(this.message)
//                .userEmail(this.userEmail)
//                .delYN(this.delYN)
//                .build();
//    }
public NotificationListDto listFromEntity() {
    return NotificationListDto.builder()
            .id(this.id)
            .qnaId(this.qnaId != null ? this.qnaId : null)
            .postId(this.postId != null ? this.postId : null)
            .message(this.message)
            .userEmail(this.userEmail)
            .delYN(this.delYN)
            .build();
}

    // DelYN 상태를 업데이트하는 메서드
    public void updateDelYN(DelYN delYN) {
        this.delYN = delYN;
    }
}
