package com.beyond.teenkiri.lecture.dto;

import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureSaveReqDto {
//    생성한 유저 id
    private String userEmail; // 🚨 멤버 생성 시 삭제

    private String title;
    private Long subjectId; // 강좌 id
    private MultipartFile video; // 강의 video
    private Float videoDuration; // 강의 video의 전체 시간
    private MultipartFile image; // 이미지 파일

    public Lecture toEntity(Subject subject) {
        return Lecture.builder()
                .title(this.title)
                .subject(subject)
                .videoDuration(this.videoDuration)
                .build();
    }
}
