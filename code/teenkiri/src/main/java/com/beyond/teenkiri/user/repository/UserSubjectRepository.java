package com.beyond.teenkiri.user.repository;

import com.beyond.teenkiri.user.domain.UserSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubjectRepository extends JpaRepository<UserSubject, Long> {

}
