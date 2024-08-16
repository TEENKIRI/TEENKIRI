package com.beyond.teenkiri.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDetailDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String imageUrl;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
