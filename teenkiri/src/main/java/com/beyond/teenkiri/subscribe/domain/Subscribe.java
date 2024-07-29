package com.beyond.teenkiri.subscribe.domain;

import com.beyond.teenkiri.common.BaseTimeEntity;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.subject.domain.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//public class Subscribe extends BaseTimeEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "subject_id")
//    private Subject subject;
//
//    private WishType wishType;
//}

/*
CREATE TABLE Subscription (
    subscribe_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    wish_type ENUM('wishlist', 'enrolled') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);

-- subscribe_id: 고유한 찜 ID. (찜 ID가 필요한지 논의 및 검토 필요)
-- user_id: 사용자 ID (외래 키, Users 테이블 참조) - 찜하거나 수강한 사용자의 식별자.
-- subject_id: 과목 ID (외래 키, Subject 테이블 참조) - 찜하거나 수강한 과목의 식별자.
-- wish_type: 행동 유형 ('wish' 또는 'enrolled') - 사용자가 과목을 찜했는지(wishlist), 수강했는지(enrolled)를 나타냄.
    -- 또는 ENUM으로 선언도 가능. -> del_yn ENUM('Y', 'N') DEFAULT 'N'  (Y는 wish N은 위시 X)
-- created_at: 생성 시각 - 결제 항목이 생성된 날짜와 시간.
* */