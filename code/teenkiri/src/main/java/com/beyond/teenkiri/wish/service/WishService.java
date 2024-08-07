package com.beyond.teenkiri.wish.service;

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

    public List<CourseListResDto> getUserWishList(User user) {
        List<Wish> wishes = wishRepository.findByUser(user);
        List<CourseListResDto> courseList = new ArrayList<>();

        for (Wish wish : wishes) {
            courseList.add(wish.getCourse().fromListEntity());
        }

        return courseList;
    }


    public void removeWish(User user, Long courseId) {
        Wish wish = wishRepository.findByUserAndCourseId(user, courseId)
                .orElseThrow(() -> new RuntimeException("찜 목록에서 해당 강좌를 찾을 수 없습니다."));
        wishRepository.delete(wish);
    }
}
