CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    address VARCHAR(255),
    email VARCHAR(100),
    user_type ENUM('student', 'teacher', 'admin') NOT NULL,
    authentication_method ENUM('kakao', 'pass', 'sns'),
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- user_id: 고유한 사용자 식별자.
-- username: 사용자의 아이디.
-- password: 암호화된 비밀번호.
-- phone: 사용자의 전화번호.
-- address: 사용자의 주소.
-- email: 사용자의 이메일 주소.
-- user_type: 사용자의 유형(student, teacher, admin).
-- authentication_method: 사용자가 인증에 사용하는 방법(kakao, pass, sns).
-- is_verified: 사용자 인증 여부.
-- created_at: 계정 생성 시각.