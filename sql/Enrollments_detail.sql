CREATE TABLE Enrollments_detail (
    enrollments_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    enrollments_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    lecture_title VARCHAR(255) NOT NULL,
    progress FLOAT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (enrollments_id) REFERENCES Enrollments(enrollment_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);


-- 각 유저의 강의 진척도.