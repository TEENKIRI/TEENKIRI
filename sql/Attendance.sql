CREATE TABLE Attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    attended BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);


-- ttendance_id: 고유한 출석 식별자.
-- user_id: 사용자의 ID.
-- attendance_date: 출석 날짜.
-- attended: 출석 여부.