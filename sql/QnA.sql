CREATE TABLE QnA (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    user_id INT NOT NULL,
    question_text TEXT NOT NULL,
    answer_text TEXT,
    answered_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES Courses(course_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (answered_by) REFERENCES Users(user_id)
);

-- question_id: 고유한 질문 식별자.
-- course_id: 질문이 속한 강좌의 ID.
-- user_id: 질문 작성자의 ID.
-- question_text: 질문 내용.
-- answer_text: 답변 내용.
-- answered_by: 답변 작성자의 ID.
-- created_at: 질문 작성 시각.