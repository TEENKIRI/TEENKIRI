-- Users 테이블
CREATE TABLE Users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    address VARCHAR(255),
    email VARCHAR(100) NOT NULL UNIQUE,
    nickname VARCHAR(100) NOT NULL UNIQUE,
    user_type ENUM('student', 'teacher', 'admin') NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    report_count INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N'
);

-- Subject 테이블
CREATE TABLE Subject (
    subject_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Subject_detail 테이블
CREATE TABLE Subject_detail (
    subject_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade ENUM('1', '2', '3', '4', '5', '6') NOT NULL,
    teacher_id BIGINT,
    subject_id BIGINT NOT NULL,
    category_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    rating FLOAT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    FOREIGN KEY (teacher_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id),
    FOREIGN KEY (category_id) REFERENCES Category(category_id)
);


-- Lectures 테이블
CREATE TABLE Lectures (
    lecture_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    lecture_title VARCHAR(255) NOT NULL,
    video_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    teacher_id BIGINT,
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id),
    FOREIGN KEY (teacher_id) REFERENCES Users(user_id)
);

-- Lectures_detail 테이블
CREATE TABLE Lectures_detail (
    lecture_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lecture_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    progress FLOAT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    FOREIGN KEY (lecture_id) REFERENCES Lectures(lecture_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Enrollments 테이블
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

-- Enrollments_detail 테이블
CREATE TABLE Enrollments_detail (
    enrollments_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    enrollments_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    subject_title VARCHAR(255) NOT NULL,
    progress FLOAT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    FOREIGN KEY (enrollments_id) REFERENCES Enrollments(enrollment_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Chats 테이블
CREATE TABLE Chats (
    chat_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subject_id BIGINT,
    message TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);

-- QnA 테이블
CREATE TABLE QnA (
    question_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    answer_text TEXT,
    answered_by BIGINT,
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (answered_by) REFERENCES Users(user_id)
);

-- Reports 테이블
CREATE TABLE Reports (
    report_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id BIGINT NOT NULL,
    reported_user_id BIGINT NOT NULL,
    chat_id BIGINT NOT NULL,
    reason ENUM('욕설', '도배', '광고', '기타'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (reporter_id) REFERENCES Users(user_id),
    FOREIGN KEY (reported_user_id) REFERENCES Users(user_id),
    FOREIGN KEY (chat_id) REFERENCES Chats(chat_id)
);

-- Reviews 테이블
CREATE TABLE Reviews (
    review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);

-- Subscription 테이블
CREATE TABLE Subscription (
    subscribe_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    wish_type ENUM('wishlist', 'enrolled') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);

-- 이벤트 스케줄러를 통해 이틀마다 Chats 테이블의 오래된 데이터를 삭제하는 이벤트 생성
CREATE EVENT IF NOT EXISTS delete_old_chats
ON SCHEDULE EVERY 1 DAY
DO
  DELETE FROM Chats
  WHERE sent_at < (NOW() - INTERVAL 2 DAY);

-- 이벤트 스케줄러를 활성화
SET GLOBAL event_scheduler = ON;



CREATE TABLE Comment (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    post_id BIGINT,
    board_type ENUM('free_board', 'QnA', 'Report') NOT NULL,
    comment_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);



CREATE TABLE Board (
    board_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id BIGINT NOT NULL,
    board_type ENUM('FREE_BOARD', 'NOTICE', 'REPORT') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    FOREIGN KEY (author_id) REFERENCES Users(user_id)
);


CREATE TABLE Category (
    category_id int AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
);


CREATE TABLE Notification ( 
    notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
