CREATE TABLE Reviews (
    review_id bigint AUTO_INCREMENT PRIMARY KEY,
    user_id bigint NOT NULL,
    subject_id bigint NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);

-- review_id: 고유한 리뷰 식별자.
-- user_id: 리뷰 작성자의 ID.
-- subject_id: 리뷰 대상 강좌의 ID.
-- rating: 강좌 평점(1~5).
-- review_text: 리뷰 내용.
-- created_at: 리뷰 작성 시각.
-- del_yn : 리뷰 삭제 여부
-- update_at: 리뷰 수정 시각