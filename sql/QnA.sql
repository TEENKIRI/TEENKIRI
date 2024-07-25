CREATE TABLE QnA (
    question_id bigint AUTO_INCREMENT PRIMARY KEY,
    subject_id bigint NOT NULL,
    user_id bigint NOT NULL,
    question_text TEXT NOT NULL,
    answer_text TEXT,
    answered_by bigint,
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (answered_by) REFERENCES Users(user_id)
);


-- question_id: 고유한 질문 식별자.
-- subject_id: 질문이 속한 강좌의 ID.
-- user_id: 질문 작성자의 ID.
-- question_text: 질문 내용.
-- answer_text: 답변 내용.
-- answered_by: 답변 작성자의 ID.
-- created_at: 질문 작성 시각.
-- answered_at: 답변 작성 시각.
-- del_yn: 게시판 질문 삭제 여부
-- update_at: 게시판 수정 시각