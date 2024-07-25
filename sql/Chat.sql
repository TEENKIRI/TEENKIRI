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
-- course_id: 해당 강좌의 ID (선택 사항임).
-- message: 채팅 메시지 내용.
-- sent_at: 메시지 전송 시각.
-- FOREIGN KEY (user_id) REFERENCES Users(user_id): 사용자의 외래키 제약 조건.
-- FOREIGN KEY (course_id) REFERENCES Courses(course_id): 강좌의 외래키 제약 조건 (옵션).


-- mySql에서 아래와 같은 이벤트 스케줄러를 통해 이틀마다 데이터를 삭제하도록 이벤트를 생성할 수 있음.
CREATE EVENT IF NOT EXISTS delete_old_chats
ON SCHEDULE EVERY 1 DAY
DO
  DELETE FROM Chats
  WHERE sent_at < (NOW() - INTERVAL 2 DAY);

-- 이벤트 스케줄러를 활성화하는 방법?
SET GLOBAL event_scheduler = ON;

