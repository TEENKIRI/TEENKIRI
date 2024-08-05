package com.beyond.teenkiri.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureDetPerUserResDto {
    private Long id;
    private String title;
    private String imageUrl;
    private String videoUrl;
    private Float progress; // 유저별 progress
    private Integer userLectureDuration; // 유저별 강의 본 시간
    private Integer videoDuration; // 강의 video의 전체 시간
    private Long enrollmentId; //해당 유저의 진행률 Id
    private Boolean isCompleted;

    private LocalDateTime updatedTime;
    private LocalDateTime createdTime;
}
