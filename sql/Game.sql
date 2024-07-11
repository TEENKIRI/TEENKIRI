CREATE TABLE Games (
    game_id bigint AUTO_INCREMENT PRIMARY KEY,
    user_id bigint NOT NULL,
    game_type ENUM('running', 'climbing') NOT NULL,
    coins_bet bigint NOT NULL,
    coins_won bigint DEFAULT 0,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);


-- game_id: 고유한 게임 식별자.
-- user_id: 사용자의 ID.
-- game_type: 게임 유형(running, climbing).
-- coins_bet: 베팅한 코인 수.
-- coins_won: 획득한 코인 수.
-- played_at: 게임 플레이 시각