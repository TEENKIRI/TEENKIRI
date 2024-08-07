package com.beyond.teenkiri.course.dto;

import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseSaveReqDto {
    private String userEmail; // 🚨 멤버 생성 시 삭제

    private String title;

    public Course toEntity(User user){
        return Course.builder()
                .title(this.title)
                .user(user)
                .build();
    }
}
