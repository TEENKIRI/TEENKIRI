CREATE TABLE Lectures_detail (
    lecture_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lecture_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    progress FLOAT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lecture_id) REFERENCES Lectures(lecture_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- 각 유저의 강의 진척도.