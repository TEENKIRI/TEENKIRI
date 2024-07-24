-- Users 테이블: 사용자 정보를 저장하는 테이블
CREATE TABLE Users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 사용자 ID
    username VARCHAR(50) NOT NULL,  -- 사용자 이름
    password VARCHAR(255) NOT NULL,  -- 사용자 비밀번호
    phone VARCHAR(20) NOT NULL UNIQUE,  -- 사용자 전화번호
    address VARCHAR(255),  -- 사용자 주소
    email VARCHAR(100) NOT NULL UNIQUE,  -- 사용자 이메일
    nickname VARCHAR(100) NOT NULL UNIQUE,  -- 사용자 닉네임
    user_type ENUM('student', 'teacher', 'admin') NOT NULL,  -- 사용자 유형 (학생, 교사, 관리자)
    is_verified BOOLEAN DEFAULT FALSE,  -- 이메일 인증 여부
    report_count INT,  -- 신고 횟수
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 수정 시간
    deleted_at TIMESTAMP,  -- 삭제 시간
    del_yn ENUM('N', 'Y') DEFAULT 'N'  -- 삭제 여부 (N: 삭제되지 않음, Y: 삭제됨)
);

-- Subject 테이블: 과목 정보를 저장하는 테이블
CREATE TABLE Subject (
    subject_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 과목 ID, 기본 키
    title VARCHAR(255) NOT NULL  -- 과목 제목
);

-- Subject_detail 테이블: 강좌 상세 정보를 저장하는 테이블
CREATE TABLE Subject_detail (
    subject_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 강좌 상세 ID
    grade ENUM('1', '2', '3', '4', '5', '6') NOT NULL,  -- 학년
    teacher_id BIGINT,  -- 교사 ID
    subject_id BIGINT,  -- 과목 ID
    title VARCHAR(255) NOT NULL,  -- 강좌 상세 제목
    description TEXT,  -- 강좌 설명
    rating FLOAT DEFAULT 0,  -- 강좌 평점
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 수정 시간
    del_yn ENUM('N', 'Y') DEFAULT 'N',  -- 삭제 여부 (N: 삭제되지 않음, Y: 삭제됨)
    FOREIGN KEY (teacher_id) REFERENCES Users(user_id), 
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id) 
);

-- Lectures 테이블: 강의 정보를 저장하는 테이블
CREATE TABLE Lectures (
    lecture_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 강의 ID
    subject_id BIGINT NOT NULL,  -- 과목 ID
    user_id BIGINT,  -- 사용자 ID
    lecture_title VARCHAR(255) NOT NULL,  -- 강의 제목
    video_url TEXT,  -- 비디오 URL
    image_url TEXT,  -- 이미지 URL
    progress FLOAT DEFAULT 0,  -- 진행 상황
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 수정 시간
    del_yn ENUM('N', 'Y') DEFAULT 'N',  -- 삭제 여부 (N: 삭제되지 않음, Y: 삭제됨)
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id),  
    FOREIGN KEY (user_id) REFERENCES Users(user_id)  
);

-- Enrollments 테이블: 학생 등록 정보를 저장하는 테이블
CREATE TABLE Enrollments (
    enrollment_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 등록 ID
    user_id BIGINT NOT NULL,  -- 사용자 ID
    subject_id BIGINT NOT NULL,  -- 과목 ID
    progress FLOAT DEFAULT 0,  -- 진행 상황
    is_completed BOOLEAN DEFAULT FALSE,  -- 완료 여부
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 수정 시간
    del_yn ENUM('N', 'Y') DEFAULT 'N',  -- 삭제 여부 (N: 삭제되지 않음, Y: 삭제됨)
    FOREIGN KEY (user_id) REFERENCES Users(user_id),  
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)  
);

-- Chats 테이블: 채팅 메시지를 저장하는 테이블
CREATE TABLE Chats (
    chat_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 채팅 ID
    user_id BIGINT NOT NULL,  -- 사용자 ID
    subject_id BIGINT,  -- 과목 ID
    message TEXT NOT NULL,  -- 메시지 내용
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 전송 시간
    FOREIGN KEY (user_id) REFERENCES Users(user_id),  
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)  
);

-- QnA 테이블: 질문과 답변을 저장하는 테이블
CREATE TABLE QnA (
    question_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 질문 ID
    subject_id BIGINT NOT NULL,  -- 과목 ID
    user_id BIGINT NOT NULL,  -- 사용자 ID
    question_text TEXT NOT NULL,  -- 질문 내용
    answer_text TEXT,  -- 답변 내용
    answered_by BIGINT,  -- 답변자 ID
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 답변 시간
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 수정 시간
    del_yn ENUM('N', 'Y') DEFAULT 'N',  -- 삭제 여부 (N: 삭제되지 않음, Y: 삭제됨)
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id), 
    FOREIGN KEY (user_id) REFERENCES Users(user_id), 
    FOREIGN KEY (answered_by) REFERENCES Users(user_id)  
);

-- Reports 테이블: 신고 정보를 저장하는 테이블
CREATE TABLE Reports (
    report_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 신고 ID
    reporter_id BIGINT NOT NULL,  -- 신고자 ID
    reported_user_id BIGINT NOT NULL,  -- 신고된 사용자 ID
    chat_id BIGINT NOT NULL,  -- 채팅 ID
    reason ENUM('욕설', '도배', '광고', '기타'),  -- 신고 이유
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 수정 시간
    FOREIGN KEY (reporter_id) REFERENCES Users(user_id),  
    FOREIGN KEY (reported_user_id) REFERENCES Users(user_id), 
    FOREIGN KEY (chat_id) REFERENCES Chats(chat_id)  
);

-- Reviews 테이블: 리뷰 정보를 저장하는 테이블
CREATE TABLE Reviews (
    review_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 리뷰 ID
    user_id BIGINT NOT NULL,  -- 사용자 ID
    subject_id BIGINT NOT NULL,  -- 과목 ID
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),  -- 평점 (1~5)
    review_text TEXT,  -- 리뷰 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 수정 시간
    del_yn ENUM('N', 'Y') DEFAULT 'N',  -- 삭제 여부 (N: 삭제되지 않음, Y: 삭제됨)
    FOREIGN KEY (user_id) REFERENCES Users(user_id),  
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)  
);

-- Subscription 테이블: 구독 정보를 저장하는 테이블
CREATE TABLE Subscription (
    subscribe_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 구독 ID
    user_id BIGINT NOT NULL,  -- 사용자 ID
    subject_id BIGINT NOT NULL,  -- 과목 ID
    wish_type ENUM('wishlist', 'enrolled') NOT NULL,  -- 구독 유형 (위시리스트, 등록)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
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

-- Comment 테이블: 댓글 정보를 저장하는 테이블
CREATE TABLE Comment (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 댓글 ID
    user_id BIGINT,  -- 사용자 ID
    board_id BIG
);
