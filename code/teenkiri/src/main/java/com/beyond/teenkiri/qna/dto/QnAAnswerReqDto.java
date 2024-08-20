package com.beyond.teenkiri.qna.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnAAnswerReqDto {
//    private String answererEmail;
    private String answererNickname;
    private String answerText;
    private LocalDateTime answeredAt;
//    private LocalDateTime createdTime;
    @Builder.Default
    private DelYN delYN = DelYN.N;
    private MultipartFile aImage;

//    public QnA toEntity(User answererEmail, QnA existingQnA) {
//        QnA qna = QnA.builder()
//                .id(existingQnA.getId())
//                .title(existingQnA.getTitle())
//                .questionText(existingQnA.getQuestionText())
//                .answerText(this.answerText)
//                .user(existingQnA.getUser())
//                .answerer(answererEmail)
//                .answeredAt(existingQnA.getAnsweredAt() != null ? existingQnA.getAnsweredAt() : LocalDateTime.now())
//                .delYN(existingQnA.getDelYN())
//                .comments(existingQnA.getComments())
//                .build();
//        qna.patchCreateTime(existingQnA.getCreatedTime());
//        return qna;
//    }
}
