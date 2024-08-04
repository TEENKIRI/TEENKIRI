package com.beyond.teenkiri.mypage.service;

import com.beyond.teenkiri.enrollment.dto.EnrollListResDto;
import com.beyond.teenkiri.enrollment.repository.EnrollmentRepository;
import com.beyond.teenkiri.favorite.domain.Favorite;
import com.beyond.teenkiri.favorite.repository.FavoriteRepository;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.dto.QnAListResDto;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.dto.UserUpdateReqDto;
import com.beyond.teenkiri.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MypageService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final QnARepository qnaRepository;
    private final FavoriteRepository favoriteRepository;

    @Autowired
    public MypageService(UserRepository userRepository, EnrollmentRepository enrollmentRepository, QnARepository qnaRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.qnaRepository = qnaRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public Page<EnrollListResDto> getUserEnrollments(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return enrollmentRepository.findAllByUserId(userId, pageable).map(enrollment -> {
            EnrollListResDto dto = new EnrollListResDto();
            dto.setId(enrollment.getId());
            dto.setProgress(enrollment.getProgress());
            return dto;
        });
    }

    public void updateUserInfo(UserUpdateReqDto userUpdateReqDto) {
        User user = userRepository.findById(userUpdateReqDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setNickname(userUpdateReqDto.getNickname());
        user.setPassword(userUpdateReqDto.getPassword());
        user.setAddress(userUpdateReqDto.getAddress());
        userRepository.save(user);
    }

    public Page<QnAListResDto> getUserQuestions(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return qnaRepository.findByUser(user, pageable).map(QnA::listFromEntity);
    }

    public List<Subject> getUserFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Favorite> favorites = favoriteRepository.findByUser(user);
        return favorites.stream()
                .map(Favorite::getSubject)
                .collect(Collectors.toList());
    }
}
