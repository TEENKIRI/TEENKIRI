package com.beyond.teenkiri.post.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByDelYN(DelYN delYN, Pageable pageable);
    List<Post> findByDelYN(DelYN delYN);
}
