package com.beyond.teenkiri.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSubjectListResDto {
    private String userEmail;
    private List<String> subjectTitles;  // 수강한 모든 강의의 제목을 리스트로 연결
    private int subjectCount;      // 여태까지 들은 강의의 개수
}
