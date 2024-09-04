package com.beyond.teenkiri.user.repository;

import com.beyond.teenkiri.user.domain.DelUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DelUserRepository extends JpaRepository<DelUser, Long> {
    boolean existsByEmail(String email);
}
