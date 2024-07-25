package com.beyond.board_demo.notice.dto;

import com.beyond.board_demo.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeUpdateDto {
    private String title;
    private String content;

}
