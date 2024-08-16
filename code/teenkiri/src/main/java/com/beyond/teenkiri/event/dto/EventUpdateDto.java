package com.beyond.teenkiri.event.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventUpdateDto {
    private String title;
    private String content;
    private String userEmail;
    private MultipartFile image;

}
