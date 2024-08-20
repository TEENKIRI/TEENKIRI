package com.beyond.teenkiri.subject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectListResDto {
    private Long id;
    private String title;
    private String teacherName;
    private Boolean isSubscribe;
    private String subjectThumUrl;

    private LocalDateTime updatedTime;
    private LocalDateTime createdTime;

    public SubjectListResDto(Long id, String title, String subjectThumUrl, String teacherName, boolean isSubscribe) {
        this.id = id;
        this.title = title;
        this.subjectThumUrl = subjectThumUrl;
        this.teacherName = teacherName;
        this.isSubscribe = isSubscribe;
    }
}
