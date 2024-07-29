package com.beyond.teenkiri.user.repository;

import com.beyond.teenkiri.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

}
