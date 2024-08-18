package com.beyond.teenkiri.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectInfoDto {

    private Long id;
    private String title;
    private String teacherName;
    private String subjectThumUrl;

    @Builder.Default
    private boolean isFavorite = true;
}
