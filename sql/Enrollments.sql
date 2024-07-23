CREATE TABLE Enrollments (
    enrollment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    enrollment_title VARCHAR(255) NOT NULL,
    progress FLOAT DEFAULT 0,
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);

-- 학생 입장에서

-- enrollment_id: 고유한 등록 식별자.
-- user_id: 등록한 사용자의 ID.
-- subject_id: 등록한 강좌의 ID.
-- progress: 강좌 진행률.
-- is_completed: 강좌 완료 여부.
-- created_at: 등록 시각.
-- del_yn : 강좌 삭제 여부
-- update_at: 강좌 수정 시각