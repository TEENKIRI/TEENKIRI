package com.beyond.teenkiri.user.sevice;

import com.beyond.teenkiri.enrollment.service.EnrollmentService;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.service.SubjectService;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.UserSubject;
import com.beyond.teenkiri.user.dto.UserSubjectSaveReqDto;
import com.beyond.teenkiri.user.repository.UserSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserSubjectService {
    private final UserSubjectRepository userSubjectRepository;
    private final SubjectService subjectService;
    private final UserService userService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public UserSubjectService(UserSubjectRepository userSubjectRepository, SubjectService subjectService, UserService userService, EnrollmentService enrollmentService) {
        this.userSubjectRepository = userSubjectRepository;
        this.subjectService = subjectService;
        this.userService = userService;
        this.enrollmentService = enrollmentService;
    }

    public UserSubject userSubjectSign(UserSubjectSaveReqDto reqDto){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);

        Subject subject = subjectService.findSubjectById(reqDto.getSubjectId());
        UserSubject userSubject = reqDto.toEntity(subject,user);

        userSubjectRepository.save(userSubject);
        return userSubject;
    }
}
