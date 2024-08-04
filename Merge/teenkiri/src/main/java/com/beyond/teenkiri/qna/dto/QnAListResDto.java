//package com.beyond.teenkiri.qna.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class QnAListResDto {
//    private Long id;
//    private String questionUserName;
//    private String title;
//    private LocalDateTime createdTime;
//}

package com.beyond.teenkiri.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnAListResDto {
    private Long id;
    private String questionUserName;
    private String title;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private LocalDateTime answeredAt;
    private String answerText; // 수정
}