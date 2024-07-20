package com.beyond.board_demo.comment.domain;

import com.beyond.board_demo.common.BaseTimeEntity;
import com.beyond.board_demo.post.domain.Post;
import com.beyond.board_demo.qna.domain.QnA;
import com.beyond.board_demo.notice.domain.Notice;
import com.beyond.board_demo.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comment")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "qna_id")
    private QnA qna;

    @ManyToOne
    @JoinColumn(name = "notice_id")
    private Notice notice;
}
