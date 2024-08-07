package com.beyond.teenkiri.wish.repository;

import com.beyond.teenkiri.wish.domain.Wish;
import com.beyond.teenkiri.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByUser(User user);
    Optional<Wish> findByUserAndCourseId(User user, Long courseId);
}
