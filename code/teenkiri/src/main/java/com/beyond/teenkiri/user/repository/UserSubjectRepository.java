package com.beyond.teenkiri.user.repository;

import com.beyond.teenkiri.user.domain.UserSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubjectRepository extends JpaRepository<UserSubject, Long> {

    Optional<UserSubject> findBySubjectIdAndUserId(Long subjectId, Long userId);
    List<UserSubject> findByUserId(Long userId);
    List<UserSubject> findByUserEmail(String userEmail);
}
