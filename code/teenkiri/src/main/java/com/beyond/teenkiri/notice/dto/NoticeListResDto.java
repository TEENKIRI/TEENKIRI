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
public class NoticeListResDto {
    private Long id;
    private String title;
    private String nickname;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}

