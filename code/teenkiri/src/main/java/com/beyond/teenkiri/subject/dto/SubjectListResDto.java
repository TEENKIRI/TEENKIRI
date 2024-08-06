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

    private LocalDateTime updatedTime;
    private LocalDateTime createdTime;
}
