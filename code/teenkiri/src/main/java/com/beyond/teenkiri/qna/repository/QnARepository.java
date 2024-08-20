package com.beyond.teenkiri.qna.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnARepository extends JpaRepository<QnA, Long> {
    Page<QnA> findAll(Pageable pageable);
    List<QnA> findByUser(User user);
    Page<QnA> findByDelYN(DelYN delYN, Pageable pageable);
    Page<QnA> findByTitleContainingIgnoreCaseAndDelYN(String title, DelYN delYN, Pageable pageable);

    // 작성자 닉네임으로 검색
    Page<QnA> findByUser_NicknameContainingIgnoreCaseAndDelYN(String nickname, DelYN delYN, Pageable pageable);

    // 강좌명으로 검색
    Page<QnA> findBySubjectTitleContainingIgnoreCaseAndDelYN(String subjectTitle, DelYN delYN, Pageable pageable);

    // 제목, 작성자, 강좌명을 모두 포함하는 검색
    Page<QnA> findByTitleContainingIgnoreCaseOrUser_NicknameContainingIgnoreCaseOrSubjectTitleContainingIgnoreCaseAndDelYN(
            String title, String nickname, String subjectTitle, DelYN delYN, Pageable pageable);

    // 특정 강좌에 해당하는 QnA 목록 가져오기
    Page<QnA> findAllBySubjectIdAndDelYN(Long subjectId, DelYN delYN, Pageable pageable);

}