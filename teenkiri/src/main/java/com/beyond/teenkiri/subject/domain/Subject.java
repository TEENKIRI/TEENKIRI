package com.beyond.teenkiri.subject.domain;

import com.beyond.teenkiri.common.BaseTimeEntity;
import com.beyond.teenkiri.common.DelYN;
import com.beyond.teenkiri.domain.User;
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
public class Subject extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 255)
    private String title;

    private Grade grade;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User teacher_id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private Float rating = 0F;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;

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
