CREATE TABLE Subject (
    subject_id INT AUTO_INCREMENT PRIMARY KEY,
    category ENUM('Korean', 'English', 'Math', 'Science') NOT NULL,
    grade ENUM('1', '2', '3', '4', '5', '6') NOT NULL,
    teacher_id INT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    rating FLOAT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_id) REFERENCES Users(user_id)
);


-- subject_id: 고유한 강좌 식별자.
-- category: 강좌의 카테고리(Korean, English, Math, Science).
-- grade: 강좌 대상 학년(1~6).
-- teacher_id: 강좌를 등록한 강사의 사용자 ID.
-- title: 강좌 제목.
-- description: 강좌 설명.
-- rating: 강좌 평점.
-- created_at: 강좌 생성 시각.
-- FOREIGN KEY (teacher_id) REFERENCES Users(user_id): 강좌를 등록한 강사의 외래키 제약 조건.