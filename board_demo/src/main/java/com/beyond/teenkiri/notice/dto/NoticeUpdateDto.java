package com.beyond.teenkiri.notice.dto;

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
