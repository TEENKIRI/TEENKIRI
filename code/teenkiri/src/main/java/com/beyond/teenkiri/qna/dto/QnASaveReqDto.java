// QnASaveReqDto.java
package com.beyond.teenkiri.qna.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnASaveReqDto {
    private String title;
    private String questionText;
    private Long subjectId;
    @Builder.Default
    private DelYN delYN = DelYN.N;
    private MultipartFile qImage;

    public QnA toEntity(User user, Subject subject) {
        return QnA.builder()
                .user(user)
                .subject(subject)
                .title(this.title)
                .questionText(this.questionText)
                .delYN(this.delYN)
                .build();
    }
}
