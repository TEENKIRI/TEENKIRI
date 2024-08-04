package com.beyond.teenkiri.subject.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.subject.dto.SubjectDetResDto;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user_board.domain.user;
import com.beyond.teenkiri.subject.dto.SubjectListResDto;
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
public class Subject extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Grade grade; // 학년이 숫자로 넣는게 불가하여, 문구버전 ENUM으로 변경

    // 유저 : 선생님
    @ManyToOne
    @JoinColumn(name = "teacher_id")  // user_id가 아닌 teacher_id로 변경하여 중복 매핑 방지
    private user userTeacher;

    // 과목
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // 연결된 강의
    @OneToMany(mappedBy = "subject", cascade = CascadeType.PERSIST)
    private List<Lecture> lectures;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private Float rating = 0F;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public SubjectListResDto fromListEntity() {
        return SubjectListResDto.builder()
                .id(this.id)
                .title(this.title)
                .teacherName(this.userTeacher.getName())
                .isSubscribe(false) // 🚨 멤버로그인 여부 확인 필요
                .build();
    }

    public SubjectDetResDto fromDetEntity() {
        return SubjectDetResDto.builder()
                .id(this.id)
                .title(this.title)
                .grade(this.grade)
                .userTeacherName(this.userTeacher.getName())
                .courseTitle(this.course.getTitle())
                .description(this.description)
                .rating(this.rating)
                .delYN(this.delYN)
                .isSubscribe(false) // 🚨 멤버로그인 여부 확인 필요
                .build();
    }
}
