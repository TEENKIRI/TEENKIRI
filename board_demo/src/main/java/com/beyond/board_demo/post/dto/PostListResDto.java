package com.beyond.board_demo.post.dto;


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
//    Author 객체 그 자체를 return 하게 되면 Author 안에 Post 가 재참조되어,
//    순환참조 이슈 발생
//    private Author author;
    private String user_email;
}
