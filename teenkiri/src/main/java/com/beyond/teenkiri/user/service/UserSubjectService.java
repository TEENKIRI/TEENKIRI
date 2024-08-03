package com.beyond.teenkiri.user.service;

import com.beyond.teenkiri.enrollment.dto.EnrollSaveReqDto;
import com.beyond.teenkiri.enrollment.service.EnrollmentService;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.service.SubjectService;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.UserSubject;
import com.beyond.teenkiri.user.dto.UserSubjectSaveReqDto;
import com.beyond.teenkiri.user.repository.UserSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Subject subject = subjectService.findSubjectById(reqDto.getSubjectId());
        User user = userService.findByEmail(reqDto.getUserEmail());
        UserSubject userSubject = reqDto.toEntity(subject,user);

        userSubjectRepository.save(userSubject);
        return userSubject;
    }
}
