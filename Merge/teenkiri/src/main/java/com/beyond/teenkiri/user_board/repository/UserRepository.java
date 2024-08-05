package com.beyond.teenkiri.user_board.repository;

import com.beyond.teenkiri.user_board.domain.user;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository("userBoardRepository")
public interface UserRepository extends JpaRepository<user, Long> {
    Optional<user> findByEmail(String email);
    Page<user> findAll(Pageable pageable);

}
