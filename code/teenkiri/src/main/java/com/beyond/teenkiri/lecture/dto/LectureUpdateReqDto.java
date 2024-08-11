package com.beyond.teenkiri.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureUpdateReqDto {
    private Long id;
    private String title;
    private Integer videoDuration;
}
