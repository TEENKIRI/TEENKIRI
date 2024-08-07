package com.beyond.teenkiri.wish.service;

import com.beyond.teenkiri.subject.dto.SubjectListResDto;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.wish.domain.Wish;
import com.beyond.teenkiri.wish.repository.WishRepository;
import com.beyond.teenkiri.course.dto.CourseListResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishService {

    @Autowired
    private WishRepository wishRepository;

    public List<SubjectListResDto> getUserWishList(User user) {
        List<Wish> wishes = wishRepository.findByUser(user);
        List<SubjectListResDto> subjectList = new ArrayList<>();

        for (Wish wish : wishes) {
            subjectList.add(wish.getSubject().fromListEntity());
        }

        return subjectList;
    }


    public void removeWish(User user, Long subjectId) {
        Wish wish = wishRepository.findByUserAndSubject_id(user, subjectId)
                .orElseThrow(() -> new RuntimeException("찜 목록에서 해당 강좌를 찾을 수 없습니다."));
        wishRepository.delete(wish);
    }
}
