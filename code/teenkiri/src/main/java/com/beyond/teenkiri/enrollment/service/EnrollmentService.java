package com.beyond.teenkiri.enrollment.service;

import com.beyond.teenkiri.enrollment.domain.CalType;
import com.beyond.teenkiri.enrollment.domain.Enrollment;
import com.beyond.teenkiri.enrollment.dto.*;
import com.beyond.teenkiri.enrollment.repository.EnrollmentRepository;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.lecture.repository.LectureRepository;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
//    private final LectureService lectureService; // 순환참조 발생
    private final LectureRepository lectureRepository;
    private final UserService userService;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, LectureRepository lectureRepository, UserService userService) {
        this.enrollmentRepository = enrollmentRepository;
        this.lectureRepository = lectureRepository;
        this.userService = userService;
    }

    public Page<EnrollListResDto> enrollList(Pageable pageable){
        Page<Enrollment> enrollments = enrollmentRepository.findAll(pageable);
        Page<EnrollListResDto> enrollListResDto = enrollments.map(a->a.fromListEntity());
        return enrollListResDto;
    }

//    public Page<EnrollListResDto> enrollListByGroup(Long subjectId, Pageable pageable){
//        Page<Enrollment> enrollments = enrollmentRepository.findAllBySubjectId(subjectId, pageable);
//        Page<EnrollListResDto> enrollListResDto = enrollments.map(a->a.fromListEntity());
//        return enrollListResDto;
//    }

    public EnrollDetResDto enrollDetail(Long id){
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("없는 진행률 입니다."));
        EnrollDetResDto enrollDetResDto = enrollment.fromDetEntity();
        return enrollDetResDto;
    }

    public Enrollment enrollCreate(EnrollSaveReqDto dto){
        Lecture lecture = lectureRepository.findById(dto.getLectureId())
                .orElseThrow(() -> new EntityNotFoundException("없는 강의입니다."));
        User user = userService.findByEmail(dto.getUserEmail());
        Enrollment enrollment = dto.toEntity(lecture,user);
        enrollmentRepository.save(enrollment);
        return enrollment;
    }

    public Enrollment enrollAutoCreate(EnrollSaveReqDto dto){
        Lecture lecture = lectureRepository.findById(dto.getLectureId())
                .orElseThrow(() -> new EntityNotFoundException("없는 강의입니다."));
        User user = userService.findByEmail(dto.getUserEmail());
        Enrollment enrollment = dto.toEntity(lecture,user);
        enrollmentRepository.save(enrollment);
        return enrollment;
    }

    public Enrollment enrollUserDurationUpdate(Long id, EnrollUpdateUserDurationReqDto dto){
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("없는 진행률 입니다."));

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);

        if(user != null && !enrollment.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("로그인 된 유저의 진행률이 아닙니다.");
        }

        Float updatedProgress =  calcProgress(CalType.DURATION, dto.getUserLectureDuration(), null, enrollment);
        enrollment.updateDuration(dto, updatedProgress);
        enrollmentRepository.save(enrollment);
        return enrollment;
    }

    public Enrollment enrollCompletedUpdate(Long id, EnrollUpdateCompletedReqDto dto){
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("없는 진행률 입니다."));

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);

        if(user != null && !enrollment.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("로그인 된 유저의 진행률이 아닙니다.");
        }

        Float updatedProgress =  calcProgress(CalType.ISCOMPLETED, null, dto.getIsCompleted(), enrollment);
        enrollment.updateCompleted(dto,updatedProgress);
        enrollmentRepository.save(enrollment);
        return enrollment;
    }

    public Enrollment findByLectureAndUser(User user, Lecture lecture){
        Enrollment enrollment = enrollmentRepository.findByLectureIdAndUserId(user.getId(), lecture.getId())
                .orElseThrow(()-> new EntityNotFoundException("없는 진행률 입니다."));
        return enrollment;
    }

    public Enrollment findByLectureIdAndUserIdRequired(User user, Lecture lecture){
        Enrollment enrollment = enrollmentRepository.findByLectureIdAndUserId(user.getId(), lecture.getId())
                .orElseThrow(()-> new EntityNotFoundException("없는 진행률 입니다."));
        return enrollment;
    }

    public Enrollment findByLectureIdAndUserId(User user, Lecture lecture){
        Enrollment enrollment = enrollmentRepository.findByLectureIdAndUserId(user.getId(), lecture.getId())
                .orElse(null);
        return enrollment;
    }

    public Float calcProgress(CalType calType, Integer userLectureDuration, Boolean isCompleted, Enrollment enrollment){
        Float returnProgress = null;
        if (calType == CalType.DURATION){ // duration 으로 계산 요청
            Float videoDurationTime = Float.valueOf(enrollment.getLecture().getVideoDuration());
            Float percentage = (userLectureDuration / videoDurationTime) * 100;
            returnProgress = (float) (Math.round(percentage * 10.0) / 10.0); // 소수점 첫 번째 자리까지만 존재하도록 반올림
            returnProgress = (returnProgress >= 99) ? 99F : returnProgress;
        }else if(calType == CalType.ISCOMPLETED){ // isCompleted로 계산
            if (isCompleted == true){ // 수강완료
                returnProgress = 100F;
            }else {
                returnProgress = 99F;
            }
        }

        return returnProgress;
    }
}
