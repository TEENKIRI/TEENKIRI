package com.beyond.teenkiri.wish.repository;

import com.beyond.teenkiri.user.domain.UserSubject;
import com.beyond.teenkiri.wish.domain.Wish;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    Optional<Wish> findByUserAndSubject(User user, Subject subject);
    List<Wish> findByUser(User user);

    Optional<Wish> findBySubjectIdAndUserId(Long subjectId, Long userId);
}
