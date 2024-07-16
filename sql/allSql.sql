CREATE TABLE Chats ( 
    chat_id bigint AUTO_INCREMENT PRIMARY KEY,
    user_id bigint NOT NULL,
    subject_id bigint,
    message TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);

-- chat_id: 고유한 채팅 식별자.
-- user_id: 메시지를 보낸 사용자의 ID.
-- subject_id: 해당 강좌의 ID (선택 사항임).
-- message: 채팅 메시지 내용.
-- sent_at: 메시지 전송 시각.
-- FOREIGN KEY (user_id) REFERENCES Users(user_id): 사용자의 외래키 제약 조건.
-- FOREIGN KEY (subject_id) REFERENCES Subject(subject_id): 강좌의 외래키 제약 조건 (옵션).

CREATE TABLE Reports (
    report_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id BIGINT NOT NULL,
    reported_user_id BIGINT NOT NULL,
    chat_id BIGINT NOT NULL,
    reason TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reporter_id) REFERENCES Users(user_id),
    FOREIGN KEY (reported_user_id) REFERENCES Users(user_id),
    FOREIGN KEY (chat_id) REFERENCES Chats(chat_id)
);

-- report_id: 고유한 신고 식별자.
-- reporter_id: 신고를 한 사용자의 ID.
-- reported_user_id: 신고된 사용자의 ID.
-- chat_id: 신고된 채팅 메시지의 ID.
-- reason: 신고 이유.
-- created_at: 신고 접수 시각.
-- FOREIGN KEY (reporter_id) REFERENCES Users(user_id): 신고한 사용자의 외래키 제약 조건.
-- FOREIGN KEY (reported_user_id) REFERENCES Users(user_id): 신고된 사용자의 외래키 제약 조건.
-- FOREIGN KEY (chat_id) REFERENCES Chats(chat_id): 신고된 채팅 메시지의 외래키 제약 조건.


-- mySql에서 아래와 같은 이벤트 스케줄러를 통해 이틀마다 데이터를 삭제하도록 이벤트를 생성할 수 있음.
CREATE EVENT IF NOT EXISTS delete_old_chats
ON SCHEDULE EVERY 1 DAY
DO
  DELETE FROM Chats
  WHERE sent_at < (NOW() - INTERVAL 2 DAY);

-- 이벤트 스케줄러를 활성화하는 방법?
SET GLOBAL event_scheduler = ON;


CREATE TABLE Enrollments (
    enrollment_id bigint AUTO_INCREMENT PRIMARY KEY,
    user_id bigint NOT NULL,
    subject_id bigint NOT NULL,
    progress float DEFAULT 0,
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);


-- enrollment_id: 고유한 등록 식별자.
-- user_id: 등록한 사용자의 ID.
-- subject_id: 등록한 강좌의 ID.
-- progress: 강좌 진행률.
-- is_completed: 강좌 완료 여부.
-- created_at: 등록 시각.


CREATE TABLE Lectures (
    lecture_id bigint AUTO_INCREMENT PRIMARY KEY,
    subject_id bigint NOT NULL,
    title VARCHAR(255) NOT NULL,
    video_url text,
    progress float DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    teacher_id bigint,
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id),
    FOREIGN KEY (teacher_id) REFERENCES Users(user_id)
);



-- lecture_id: 고유한 강의 식별자.
-- subject_id: 해당 강의가 속한 강좌의 ID.
-- title: 강의 제목.
-- video_url: 강의 영상 URL. (폴더 형식으로 가는지 ?)
-- progress: 강의 진행률.
-- created_at: 강의 생성 시각.
-- teacher_id: 강의를 진행하는 강사의 ID.


CREATE TABLE QnA (
    question_id bigint AUTO_INCREMENT PRIMARY KEY,
    subject_id bigint NOT NULL,
    user_id bigint NOT NULL,
    question_text TEXT NOT NULL,
    answer_text TEXT,
    answered_by bigint,
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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


CREATE TABLE Reviews (
    review_id bigint AUTO_INCREMENT PRIMARY KEY,
    user_id bigint NOT NULL,
    subject_id bigint NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);

-- review_id: 고유한 리뷰 식별자.
-- user_id: 리뷰 작성자의 ID.
-- subject_id: 리뷰 대상 강좌의 ID.
-- rating: 강좌 평점(1~5).
-- review_text: 리뷰 내용.
-- created_at: 리뷰 작성 시각.


CREATE TABLE Subject (
    subject_id bigint AUTO_INCREMENT PRIMARY KEY,
    category ENUM('Korean', 'English', 'Math', 'Science') NOT NULL,
    grade ENUM('1', '2', '3', '4', '5', '6') NOT NULL,
    teacher_id bigint,
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



CREATE TABLE  (
    subscribe_id
    user_id bigint NOT NULL,
    subject_id bigint NOT NULL,
    action_type ENUM('wishlist', 'enrolled') NOT NULL,
    -- created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 찜 생성시각이 필요한지 의문이지만 일단 넣음
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id)
);

-- subscribe_id: 고유한 찜 ID. (찜 ID가 필요한지 논의 및 검토 필요)
-- user_id: 사용자 ID (외래 키, Users 테이블 참조) - 찜하거나 수강한 사용자의 식별자.
-- subject_id: 과목 ID (외래 키, Subject 테이블 참조) - 찜하거나 수강한 과목의 식별자.
-- action_type: 행동 유형 ('wish' 또는 'enrolled') - 사용자가 과목을 찜했는지(wishlist), 수강했는지(enrolled)를 나타냄.
    -- 또는 ENUM으로 선언도 가능. -> del_yn ENUM('Y', 'N') DEFAULT 'N'  (Y는 wish N은 위시 X)
-- created_at: 생성 시각 - 결제 항목이 생성된 날짜와 시간


CREATE TABLE Users (
    user_id bigint AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    del_yn ENUM('N', 'Y') DEFAULT 'N',
    phone varchar(20) NOT NULL UNIQUE,
    address VARCHAR(255),
    email VARCHAR(100) NOT NULL UNIQUE,
    user_type ENUM('student', 'teacher', 'admin') NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- user_id: 고유한 사용자 식별자.
-- username: 사용자의 아이디.
-- password: 암호화된 비밀번호.
-- del_yn: 회원탈퇴 여부.
-- phone: 사용자의 전화번호.
-- address: 사용자의 주소.
-- email: 사용자의 이메일 주소.
-- user_type: 사용자의 유형(student, teacher, admin).
-- is_verified: 사용자 인증 여부.
-- created_at: 계정 생성 시각.

