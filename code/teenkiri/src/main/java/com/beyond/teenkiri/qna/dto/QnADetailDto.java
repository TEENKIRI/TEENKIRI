package com.beyond.teenkiri.qna.dto;

import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.comment.dto.CommentDetailDto; // 댓글 DTO 임포트
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List; // 댓글 리스트를 위한 임포트

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnADetailDto {
    private Long id;
    private String title;
    private String questionText;
    private String answerText;
    private String questionUserNickname;
    private String answeredByNickname;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private LocalDateTime answeredAt;
    private String userEmail;
    private String qImageUrl;
    private String aImageUrl;
    private String subjectTitle;
    private Long subjectId;
    private List<CommentDetailDto> comments; // 댓글 리스트 추가

    public static QnADetailDto fromEntity(QnA qna, List<CommentDetailDto> comments) { // 댓글 리스트를 인자로 추가
        return QnADetailDto.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .questionText(qna.getQuestionText())
                .answerText(qna.getAnswerText())
                .questionUserNickname(qna.getUser().getNickname())
                .answeredByNickname(qna.getAnswerer() != null ? qna.getAnswerer().getNickname() : null)
                .createdTime(qna.getCreatedTime())
                .updatedTime(qna.getUpdatedTime())
                .answeredAt(qna.getAnsweredAt())
                .userEmail(qna.getUser().getEmail())
                .qImageUrl(qna.getQImageUrl())
                .aImageUrl(qna.getAImageUrl())
                .subjectTitle(qna.getSubject().getTitle())
                .subjectId(qna.getSubject().getId())
                .comments(comments) // 댓글 리스트 설정
                .build();
    }
}
