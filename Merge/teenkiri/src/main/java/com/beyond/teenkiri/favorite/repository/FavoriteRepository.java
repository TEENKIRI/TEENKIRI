package com.beyond.teenkiri.favorite.repository;

import com.beyond.teenkiri.favorite.domain.Favorite;
import com.beyond.teenkiri.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
}
