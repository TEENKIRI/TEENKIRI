package com.beyond.board_demo.post.repository;

import com.beyond.board_demo.post.domain.Post;
import com.beyond.board_demo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
}
