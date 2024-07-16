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