CREATE TABLE Payments (
    payment_id bigint AUTO_INCREMENT PRIMARY KEY,
    user_id bigint NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('card', 'paypal', 'coupon') NOT NULL,
    payment_type ENUM('subscription', 'single_course') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);


-- payment_id: 고유한 결제 식별자.
-- user_id: 사용자의 ID.
-- amount: 결제 금액.
-- payment_method: 결제 방법(card, paypal, coupon).
-- payment_type: 결제 유형(subscription, single_course).
-- created_at: 결제 시각.
-- FOREIGN KEY (user_id) REFERENCES Users(user_id): 사용자의 외래키 제약 조건.