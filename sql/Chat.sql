CREATE TABLE Chats ( 
chat_id bigint AUTO_INCREMENT PRIMARY KEY,
user_id bigint NOT NULL,
course_id bigint,
message TEXT NOT NULL,
sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES Users(user_id),
FOREIGN KEY (course_id) REFERENCES Courses(course_id) );


-- chat_id: 고유한 채팅 식별자.
-- user_id: 메시지를 보낸 사용자의 ID.
-- course_id: 해당 강좌의 ID (선택 사항임).
-- message: 채팅 메시지 내용.
-- sent_at: 메시지 전송 시각.
-- FOREIGN KEY (user_id) REFERENCES Users(user_id): 사용자의 외래키 제약 조건.
-- FOREIGN KEY (course_id) REFERENCES Courses(course_id): 강좌의 외래키 제약 조건 (옵션).
-- daily_game_count: 해당 사용자가 하루 동안 몇 번의 게임을 진행했는지 기록. 이를 통해 하루 10회로 게임 진행 횟수를 제한.
-- played_date: 게임을 플레이한 날짜를 저장합니다. 이 필드를 통해 날짜별로 게임 횟수를 제한하고 제한.