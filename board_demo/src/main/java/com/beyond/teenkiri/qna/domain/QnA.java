package com.beyond.teenkiri.qna.domain;

import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.dto.QnAAtoUpdateDto;
import com.beyond.teenkiri.qna.dto.QnAListResDto;
import com.beyond.teenkiri.qna.dto.QnAQtoUpdateDto;
import com.beyond.teenkiri.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "qna")
public class QnA extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 3000, nullable = false)
    private String questionText;

    @Column(length = 3000)
    private String answerText;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "answered_by")
    private User answeredBy;

    @Column
    private LocalDateTime answeredAt;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('N', 'Y') DEFAULT 'N'")
    private DelYN delYN;

    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public QnAListResDto listFromEntity() {
        return QnAListResDto.builder()
                .id(this.id)
                .questionUserName(this.getUser().getNickname())
                .title(this.getTitle())
                .createdTime(this.getCreatedTime())
                .build();
    }

    public void QnAQUpdate(QnAQtoUpdateDto dto) {
        this.questionText = dto.getQuestionText();
        this.title = dto.getTitle();
    }

    public void QnAAUpdate(QnAAtoUpdateDto dto) {
        this.answerText = dto.getAnswerText();
    }
}
