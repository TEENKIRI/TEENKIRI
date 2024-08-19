package com.beyond.teenkiri.subject.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.enrollment.domain.Enrollment;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.subject.dto.SubjectDetResDto;
import com.beyond.teenkiri.subject.dto.SubjectListResDto;
import com.beyond.teenkiri.subject.dto.SubjectUpdateReqDto;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.UserSubject;
import com.beyond.teenkiri.wish.domain.Wish;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Enumerated(EnumType.STRING)
    private Grade grade; // 학년이 숫자로 넣는게 불가하여, 문구버전 ENUM으로 변경
//    private Integer gradeEnumValue;
//    @Transient
//    private Grade getMyEnum() {
//        return Grade.fromValue(gradeEnumValue);
//    }
//    private void setMyEnum(Grade grade) {
//        this.gradeEnumValue = grade.getValue();
//    }

    //    유저 : 선생님
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userTeacher;

    //    과목
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    //    연결된 강의
    @OneToMany(mappedBy = "subject", cascade = CascadeType.PERSIST)
    private List<Lecture> lectures;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.PERSIST)
    private List<UserSubject> userSubjects;


    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private Float rating = 0F;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    private String subjectThumUrl;

    //    메인페이지, 강좌 상단에 놓는 용도의 boolean값
    @Builder.Default
    private Boolean isMainSubject = false;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime mainSubjectUpdatedDate;


    public SubjectListResDto fromListEntity(Wish wish) {
        return SubjectListResDto.builder()
                .id(this.id)
                .title(this.title)
                .teacherName(this.userTeacher.getName())
                .subjectThumUrl(this.subjectThumUrl)
                .isSubscribe((wish != null) ? true : false) // 유저 로그인 및 wish 여부 확인
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public SubjectDetResDto fromDetEntity(UserSubject userSubject, Wish wish) {
        SubjectDetResDto subjectDetResDto = SubjectDetResDto.builder()
                .id(this.id)
                .title(this.title)
                .grade(this.grade)
                .userTeacherId(this.userTeacher.getId())
                .userTeacherName(this.userTeacher.getName())
                .subjectThumUrl(this.subjectThumUrl)
                .courseTitle(this.course.getTitle())
                .description(this.description)
                .rating(this.rating)
                .delYN(this.delYN)
                .isSubscribe((wish != null) ? true : false) // 유저 로그인 및 wish 여부 확인
                .isRegistered((userSubject != null) ? true : false) // 유저 로그인 및 수강신청 여부 확인
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();

        return subjectDetResDto;
    }

    public void updateImagePath(String s3ImagePath) {
        this.subjectThumUrl = s3ImagePath;
    }

    public void updateDelYn(DelYN delYN) {
        this.delYN = delYN;
    }

    public void toUpdate(SubjectUpdateReqDto dto, User userTeacher, Course course) {
        this.userTeacher = userTeacher;
        this.title = dto.getTitle();
        this.grade = dto.getGrade();
        this.course = course;
        this.description = dto.getDescription();
    }

    public void toUpdateIsMainSubject(Boolean isMainSubject){
        this.isMainSubject = isMainSubject;
        if (isMainSubject.equals(true)){ // 상단에 고정하기로 함
            LocalDateTime currentTime = LocalDateTime.now();
            this.mainSubjectUpdatedDate = currentTime; // 고정하기로 한 날짜 기입
        }
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
}


/*
* -- Subject 테이블: 과목 정보를 저장하는 테이블
CREATE TABLE Subject (
    subject_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 과목 ID, 기본 키
    title VARCHAR(255) NOT NULL  -- 과목 제목
);

-- Subject_detail 테이블: 강좌 상세 정보를 저장하는 테이블
CREATE TABLE Subject_detail (
    subject_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 강좌 상세 ID
    grade ENUM('1', '2', '3', '4', '5', '6') NOT NULL,  -- 학년
    teacher_id BIGINT,  -- 교사 ID
    subject_id BIGINT,  -- 과목 ID
    title VARCHAR(255) NOT NULL,  -- 강좌 상세 제목
    description TEXT,  -- 강좌 설명
    rating FLOAT DEFAULT 0,  -- 강좌 평점
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 수정 시간
    del_yn ENUM('N', 'Y') DEFAULT 'N',  -- 삭제 여부 (N: 삭제되지 않음, Y: 삭제됨)
    FOREIGN KEY (teacher_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);
* */
