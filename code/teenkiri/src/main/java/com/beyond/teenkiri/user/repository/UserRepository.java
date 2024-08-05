package com.beyond.teenkiri.user.repository;

import com.beyond.teenkiri.user.domain.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNameAndPhone(String username, String phone);
    Optional<User> findByNameAndPhoneAndEmail(String username, String phone, String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}