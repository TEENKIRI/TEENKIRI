CREATE TABLE Lectures (
    lecture_id bigint AUTO_INCREMENT PRIMARY KEY,
    course_id bigint NOT NULL,
    title VARCHAR(255) NOT NULL,
    video_url VARCHAR(255),
    progress bigint DEFAULT 0,
    quiz_score INT DEFAULT 0,
    coin_reward bigint DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    teacher_id bigint,
    FOREIGN KEY (course_id) REFERENCES Courses(course_id),
    FOREIGN KEY (teacher_id) REFERENCES Users(user_id)
);



-- lecture_id: 고유한 강의 식별자.
-- course_id: 해당 강의가 속한 강좌의 ID.
-- title: 강의 제목.
-- video_url: 강의 영상 URL.
-- progress: 강의 진행률.
-- quiz_score: 강의에 대한 퀴즈 점수.
-- coin_reward: 강의 수강 시 지급되는 코인 수.
-- created_at: 강의 생성 시각.
-- teacher_id: 강의를 진행하는 강사의 ID.
-- FOREIGN KEY (course_id) REFERENCES Courses(course_id): 강좌의 외래키 제약 조건.
-- FOREIGN KEY (teacher_id) REFERENCES Users(user_id): 강사의 외래키 제약 조건