package com.beyond.teenkiri.course.domain;

// 과목 Table

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.course.dto.CourseDetResDto;
import com.beyond.teenkiri.course.dto.CourseListResDto;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Course extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    private List<Subject> subjects ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public CourseListResDto fromListEntity(){
        return CourseListResDto.builder()
                .id(this.id)
                .title(this.title)
                .updatedTime(this.getUpdatedTime())
                .createdTime(this.getCreatedTime())
                .build();
    }

    public CourseDetResDto fromDetEntity() {
        return CourseDetResDto.builder()
                .id(this.id)
                .title(this.title)
                .userName(this.getUser().getName())
                .updatedTime(this.getUpdatedTime())
                .createdTime(this.getCreatedTime())
                .build();
    }
}

/*
-- Subject 테이블: 과목 정보를 저장하는 테이블
CREATE TABLE Subject (
    subject_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 과목 ID, 기본 키
    title VARCHAR(255) NOT NULL  -- 과목 제목
);
* */