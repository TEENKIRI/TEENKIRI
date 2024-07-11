CREATE TABLE CharacterOwnership (
    ownership_id bigint AUTO_INCREMENT PRIMARY KEY,
    user_id bigint NOT NULL,
    avatar_id bigint NOT NULL,
    acquired_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (avatar_id) REFERENCES Avatars(avatar_id)
);

-- ownership_id: 고유한 소유 식별자.
-- user_id: 사용자의 ID.
-- avatar_id: 소유한 캐릭터의 ID.
-- acquired_at: 캐릭터 획득 시각.