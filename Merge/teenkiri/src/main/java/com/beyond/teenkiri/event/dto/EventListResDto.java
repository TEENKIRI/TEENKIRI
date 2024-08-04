package com.beyond.teenkiri.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventListResDto {
    private Long id;
    private String title;
    private String nickname;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
