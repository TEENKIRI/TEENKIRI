CREATE TABLE Lectures (
    lecture_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    user_id BIGINT,
    lecture_title VARCHAR(255) NOT NULL,
    video_url TEXT,
    progress FLOAT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);



-- lecture_id: 고유한 강의 식별자.
-- subject_id: 해당 강의가 속한 강좌의 ID.
-- title: 강의 제목.
-- video_url: 강의 영상 URL. (폴더 형식으로 가는지 ?)
-- progress: 강의 진행률.
-- created_at: 강의 생성 시각.
-- teacher_id: 강의를 진행하는 강사의 ID.
-- del_yn : 강의 삭제 여부.
-- update_at: 강의 수정 시각.