// QnA.java
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
    private User user; // 작성자

    @ManyToOne
    @JoinColumn(name = "answerer_id")
    private User answerer;

    @Column
    private LocalDateTime answeredAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String qImageUrl = "";

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String aImageUrl = "";

    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public QnAListResDto listFromEntity() {
        return QnAListResDto.builder()
                .id(this.id)
                .questionUserName(this.getUser().getNickname())
                .title(this.getTitle())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .answeredAt(this.answeredAt)
                .answerText(this.answerText)
                .qImageUrl(this.qImageUrl)
                .build();
    }

    // 질문
    public void QnAQUpdate(QnAQtoUpdateDto dto, String qImageUrl) {
        this.questionText = dto.getQuestionText();
        this.title = dto.getTitle();
        this.getUpdatedTime();
        this.qImageUrl = qImageUrl;
    }

    //  답변 업데이트 사용하는거고
    public void QnAAUpdate(QnAAtoUpdateDto dto, String aImageUrl) {
        this.answerText = dto.getAnswerText();
        this.answeredAt = LocalDateTime.now();
        this.aImageUrl = aImageUrl;
    }
    public void answerQuestion(String answerText, User answerer) {
        this.answerText = answerText;
        this.answerer = answerer;
        this.answeredAt = getCreatedTime();
    }

    // 답변을 생성할때 사용
    public void aUpdateImagePath(String imagePath){
        this.aImageUrl = imagePath;
    }
    public void qUpdateImagePath(String imagePath){
        this.qImageUrl = imagePath;
    }
    public void updateDelYN(DelYN delYN){
        this.delYN = delYN;
    }
}
