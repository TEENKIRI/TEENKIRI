package com.beyond.teenkiri.user.sevice;

import com.beyond.teenkiri.enrollment.service.EnrollmentService;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.service.SubjectService;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.UserSubject;
import com.beyond.teenkiri.user.dto.UserSubjectListResDto;
import com.beyond.teenkiri.user.dto.UserSubjectSaveReqDto;
import com.beyond.teenkiri.user.repository.UserSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserSubjectService {
    private final UserSubjectRepository userSubjectRepository;
    private final SubjectService subjectService;
    private final UserService userService;

    @Autowired
    public UserSubjectService(UserSubjectRepository userSubjectRepository, SubjectService subjectService, UserService userService) {
        this.userSubjectRepository = userSubjectRepository;
        this.subjectService = subjectService;
        this.userService = userService;
    }

    public UserSubject userSubjectSign(UserSubjectSaveReqDto reqDto){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);

        Subject subject = subjectService.findSubjectById(reqDto.getSubjectId());
        UserSubject userSubject = reqDto.toEntity(subject,user);

        userSubjectRepository.save(userSubject);
        return userSubject;
    }


    public UserSubjectListResDto getUserSubjects() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserSubject> userSubjects = userSubjectRepository.findByUserEmail(userEmail);

        List<String> subjectTitles = userSubjects.stream()
                .map(us -> us.getSubject().getTitle())
                .collect(Collectors.toList());
        // 위 코드는 아래 코드를 스트림으로 쓴 것입니다.
//        List<String> subjectTitles = new ArrayList<>();
//        for (UserSubject userSubject : userSubjects) {
//            String title = userSubject.getSubject().getTitle();
//            subjectTitles.add(title);
//        }

        int subjectCount = userSubjects.size();
        return userSubjects.isEmpty() ? null : userSubjects.get(0).listFromEntity(subjectTitles, subjectCount);
    }
}
