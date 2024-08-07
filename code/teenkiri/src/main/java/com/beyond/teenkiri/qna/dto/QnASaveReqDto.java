// QnASaveReqDto.java
package com.beyond.teenkiri.qna.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnASaveReqDto {
    private String title;
    private String questionText;
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public QnA toEntity(User user) {
        return QnA.builder()
                .user(user)
                .title(this.title)
                .questionText(this.questionText)
                .delYN(this.delYN)
                .build();
    }
}
