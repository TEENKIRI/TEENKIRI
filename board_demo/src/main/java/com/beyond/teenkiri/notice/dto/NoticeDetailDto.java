package com.beyond.teenkiri.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDetailDto {
    private Long id;
    private String title;
    private String content;
    private String userNickname;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;


}
