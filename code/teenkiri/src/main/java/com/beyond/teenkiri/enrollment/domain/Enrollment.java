package com.beyond.teenkiri.enrollment.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.enrollment.dto.EnrollDetResDto;
import com.beyond.teenkiri.enrollment.dto.EnrollListResDto;
import com.beyond.teenkiri.enrollment.dto.EnrollUpdateCompletedReqDto;
import com.beyond.teenkiri.enrollment.dto.EnrollUpdateUserDurationReqDto;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 강의별 진행도

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Enrollment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Builder.Default
    private Float progress = 0F;

    @Builder.Default
    private Integer userLectureDuration = 0;

    @Builder.Default
    private Boolean isCompleted = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public EnrollListResDto fromListEntity() {
        return EnrollListResDto.builder()
                .id(this.id)
                .progress(this.progress)
                .build();
    }

    public EnrollDetResDto fromDetEntity() {
        return EnrollDetResDto.builder()
                .id(this.id)
                .progress(this.progress)
                .build();
    }

    public void updateDuration(EnrollUpdateUserDurationReqDto dto, Float progress) {
        if(!this.userLectureDuration.equals(dto.getUserLectureDuration())){
            this.userLectureDuration = dto.getUserLectureDuration();
            this.progress = progress;
        }
    }


    public void updateCompleted(EnrollUpdateCompletedReqDto dto, Float progress) {
        if(!this.isCompleted.equals(dto.getIsCompleted())){
            this.isCompleted = dto.getIsCompleted();
            this.progress = progress;
        }
    }
}

/*
CREATE TABLE Enrollments (
    enrollment_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 등록 ID
    user_id BIGINT NOT NULL,  -- 사용자 ID
    subject_id BIGINT NOT NULL,  -- 과목 ID
    progress FLOAT DEFAULT 0,  -- 진행 상황
    is_completed BOOLEAN DEFAULT FALSE,  -- 완료 여부
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 수정 시간
    del_yn ENUM('N', 'Y') DEFAULT 'N',  -- 삭제 여부 (N: 삭제되지 않음, Y: 삭제됨)
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);
* */