package com.beyond.teenkiri.post.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostListResDto {
    private Long id;
    private String title;
    private String user_email;
}
