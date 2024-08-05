//package com.beyond.board_demo.comment.dto;
//
//import com.beyond.board_demo.comment.domain.Comment;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class CommentResDto {
//    private Long id;
//    private String content;
//    private String userNickname;
//    private String createdTime;
//
//    public static CommentResDto fromEntity(Comment comment) {
//        return CommentResDto.builder()
//                .id(comment.getId())
//                .content(comment.getContent())
//                .userNickname(comment.getUser().getNickname())
//                .createdTime(comment.getCreatedTime().toString())
//                .build();
//    }
//}