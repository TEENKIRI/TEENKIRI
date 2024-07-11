CREATE TABLE Attendance (
    attendance_id bigint AUTO_INCREMENT PRIMARY KEY,
    user_id bigint NOT NULL,
    attendance_date DATE NOT NULL,
    attendance_rate bigint,
    attended BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);


-- attendance_id: 고유한 출석 식별자.
-- user_id: 사용자의 ID.
-- attendance_date: 출석 날짜.
-- attendance_rate: 출석률
-- attended: 출석 여부.