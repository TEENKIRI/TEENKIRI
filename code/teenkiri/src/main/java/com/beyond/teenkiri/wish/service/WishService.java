package com.beyond.teenkiri.wish.service;

import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.dto.SubjectListResDto;
import com.beyond.teenkiri.subject.repository.SubjectRepository;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.beyond.teenkiri.wish.domain.Wish;
import com.beyond.teenkiri.wish.dto.WishDto;
import com.beyond.teenkiri.wish.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishService {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public WishDto addWish(String email, Long subjectId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("과목을 찾을 수 없습니다."));

        Wish wish = new Wish(user, subject);
        wishRepository.save(wish);

        return WishDto.fromEntity(wish);
    }

    public void removeWish(String email, Long subjectId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("과목을 찾을 수 없습니다."));

        Wish wish = wishRepository.findByUserAndSubject(user, subject)
                .orElseThrow(() -> new RuntimeException("찜 항목을 찾을 수 없습니다."));

        wishRepository.delete(wish);
    }

    public List<SubjectListResDto> getUserWishList(User user) {
        List<Wish> wishList = wishRepository.findByUser(user);
        List<SubjectListResDto> subjectList = new ArrayList<>();

        for (Wish wish : wishList) {
            Subject subject = wish.getSubject();
            SubjectListResDto subjectListResDto = new SubjectListResDto(
                    subject.getId(),
                    subject.getTitle(),
                    subject.getUserTeacher().getName(),
                    true
            );
            subjectList.add(subjectListResDto);
        }

        return subjectList;
    }
}
