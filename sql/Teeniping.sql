CREATE TABLE Avatars (
    avatar_id INT AUTO_INCREMENT PRIMARY KEY,
    avatar_name VARCHAR(50) NOT NULL,
    rank ENUM('A', 'B', 'C', 'D', 'E', 'F')
);

-- 45개의 티니핑 캐릭터 추가
INSERT INTO Avatars (avatar_name, rank)
VALUES 
    ('AvatarA', 'A'), ('AvatarB', 'B'), ..., ('AvatarAN', 'F');



-- avatar_id: 고유한 캐릭터 식별자.
-- avatar_name: 캐릭터 이름.
-- rank: 캐릭터의 랭크(A, B, C, D, E, F).