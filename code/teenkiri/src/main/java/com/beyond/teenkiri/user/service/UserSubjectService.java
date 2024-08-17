package com.beyond.teenkiri.user.service;

import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.service.SubjectService;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.UserSubject;
import com.beyond.teenkiri.user.dto.SubjectInfoDto;
import com.beyond.teenkiri.user.dto.UserSubjectListResDto;
import com.beyond.teenkiri.user.dto.UserSubjectSaveReqDto;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.beyond.teenkiri.user.repository.UserSubjectRepository;
import com.beyond.teenkiri.wish.domain.Wish;
import com.beyond.teenkiri.wish.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserSubjectService {
    private final UserSubjectRepository userSubjectRepository;
    private final SubjectService subjectService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final WishRepository wishRepository;

    @Autowired
    public UserSubjectService(UserSubjectRepository userSubjectRepository, SubjectService subjectService, UserService userService, UserRepository userRepository, WishRepository wishRepository) {
        this.userSubjectRepository = userSubjectRepository;
        this.subjectService = subjectService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.wishRepository = wishRepository;
    }

    public UserSubject userSubjectSign(UserSubjectSaveReqDto reqDto){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);

        Subject subject = subjectService.findSubjectById(reqDto.getSubjectId());
        UserSubject userSubject = reqDto.toEntity(subject,user);

        userSubjectRepository.save(userSubject);
        return userSubject;
    }


    // 타이틀, 선생님 이름, 강좌 썸네일, (찜 여부)
    public UserSubjectListResDto getUserSubjects() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserSubject> userSubjects = userSubjectRepository.findByUserEmail(userEmail);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        List<Wish> wishes = wishRepository.findByUser(user);

        List<SubjectInfoDto> subjects = userSubjects.stream()
                .map(us -> {
                    boolean isFavorite = wishes.stream()
                            .anyMatch(wish -> wish.getSubject().equals(us.getSubject()));
                    return SubjectInfoDto.builder()
                            .id(us.getSubject().getId())
                            .title(us.getSubject().getTitle())
                            .teacherName(us.getSubject().getUserTeacher().getName())
                            .subjectThumUrl(us.getSubject().getSubjectThumUrl())
                            .isFavorite(isFavorite)
                            .build();
                })
                .collect(Collectors.toList());
        int subjectCount = userSubjects.size();
        System.out.println(subjects.toString());

        return UserSubjectListResDto.builder()
                .subjects(subjects)
                .subjectCount(subjectCount)
                .build();
    }


        // 위 코드는 아래 코드를 스트림으로 쓴 것입니다.
//        List<String> subjectTitles = new ArrayList<>();
//        for (UserSubject userSubject : userSubjects) {
//            String title = userSubject.getSubject().getTitle();
//            subjectTitles.add(title);
//        }
}
