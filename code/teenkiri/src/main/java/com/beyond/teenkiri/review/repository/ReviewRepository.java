package com.beyond.teenkiri.review.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.review.domain.Review;
import com.beyond.teenkiri.user.domain.UserSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByDelYn(DelYN delYN, Pageable pageable);
    boolean existsByUserSubject(UserSubject userSubject);
    Page<Review> findByUserSubject_Subject_Id(Long subjectId, Pageable pageable);
}
