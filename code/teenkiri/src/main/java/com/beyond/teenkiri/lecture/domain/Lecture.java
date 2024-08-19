package com.beyond.teenkiri.lecture.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.enrollment.domain.Enrollment;
import com.beyond.teenkiri.lecture.dto.LectureDetPerUserResDto;
import com.beyond.teenkiri.lecture.dto.LectureDetResDto;
import com.beyond.teenkiri.lecture.dto.LectureListResDto;
import com.beyond.teenkiri.lecture.dto.LectureUpdateReqDto;
import com.beyond.teenkiri.subject.domain.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Lecture extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

//    private User user;

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String videoUrl = "";

    // 소수점이 나와도, 내림으로 진행 (추후 진행률 받을 때 이슈 생길 가능성으로 인해) : 소수점 내림은 프론트에서 진행
    @Builder.Default
    private Integer videoDuration = 0;

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String imageUrl = "";

    @Builder.Default
    private Float progress = 0F;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;


    public void updateImagePath(String imagePath){
        this.imageUrl = imagePath;
    }

    public void updateVideoPath(String videoPath,Integer videoDuration ){
        this.videoUrl = videoPath;
        this.videoDuration = videoDuration;
    }

    public LectureListResDto fromListEntity(Enrollment enrollment) {
        return LectureListResDto.builder()
                .id(this.id)
                .title(this.title)
                .imageUrl(this.imageUrl)
                .videoUrl(this.videoUrl)
                .progress((enrollment != null) ? enrollment.getProgress() : null) // 유저별 진행률
                .isCompleted((enrollment != null) ? enrollment.getIsCompleted() : null) // 유저별 진행률
                .videoDuration(this.videoDuration)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public LectureDetResDto fromDetEntity() {
        return LectureDetResDto.builder()
                .id(this.id)
                .subjectId(this.subject.getId())
                .title(this.title)
                .imageUrl(this.imageUrl)
                .videoUrl(this.videoUrl)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public LectureDetPerUserResDto fromDetPerUserEntity(Enrollment enrollment) {
        return LectureDetPerUserResDto.builder()
                .id(this.id)
                .title(this.title)
                .imageUrl(this.imageUrl)
                .videoUrl(this.videoUrl)
                .progress(enrollment.getProgress()) // 유저별 진행률
                .userLectureDuration(enrollment.getUserLectureDuration()) // 유저별 강의를 시청한 시간
                .videoDuration(this.videoDuration) // video 전체 시간 (초단위)
                .enrollmentId(enrollment.getId())
                .isCompleted(enrollment.getIsCompleted())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public void toUpdate(LectureUpdateReqDto dto) {
        if(!this.title.equals(dto.getTitle())){
            this.title = dto.getTitle();
        }
    }

    public void toDeleteUpdate() {
        this.delYN = DelYN.Y;
    }
}

/*
CREATE TABLE Lectures (
    lecture_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 강의 ID
    subject_id BIGINT NOT NULL,  -- 과목 ID
    user_id BIGINT,  -- 사용자 ID
    lecture_title VARCHAR(255) NOT NULL,  -- 강의 제목
    video_url TEXT,  -- 비디오 URL
    image_url TEXT,  -- 이미지 URL
    progress FLOAT DEFAULT 0,  -- 진행 상황
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 수정 시간
    del_yn ENUM('N', 'Y') DEFAULT 'N',  -- 삭제 여부 (N: 삭제되지 않음, Y: 삭제됨)
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
* */