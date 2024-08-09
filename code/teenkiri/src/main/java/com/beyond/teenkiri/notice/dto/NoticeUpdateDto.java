package com.beyond.teenkiri.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeUpdateDto {
    private String title;
    private String content;
    private MultipartFile image;
}
