CREATE TABLE Coins (
    coin_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    amount INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);



-- coin_id: 고유한 코인 식별자.
-- user_id: 사용자의 ID.
-- amount: 보유 코인 수.
-- updated_at: 코인 정보 업데이트 시각.