package com.beyond.board_demo.notice.dto;

import com.beyond.board_demo.notice.domain.Notice;
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
    private String userNickname;
    private LocalDateTime createdTime;
}

