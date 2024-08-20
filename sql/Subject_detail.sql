CREATE TABLE Subject_detail (
    subject_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade ENUM('1', '2', '3', '4', '5', '6') NOT NULL,
    teacher_id BIGINT,
    subject_id BIGINT ,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    rating FLOAT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    FOREIGN KEY (teacher_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id),
);

-- 등록한 강좌 입장에서

-- subject_dtail_id: 고유한 강좌 식별자.
-- grade: 강좌 대상 학년(1~6).
-- teacher_id: 강좌를 등록한 강사의 사용자 ID.
-- title: 강좌 제목.
-- description: 강좌 설명.
-- rating: 강좌 평점.
-- created_at: 강좌 생성 시각.
-- del_yn: 강좌 삭제 여부.
-- update_at: 강좌 수정 시각.