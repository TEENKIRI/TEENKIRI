package com.beyond.teenkiri.enrollment.service;

import com.beyond.teenkiri.enrollment.domain.Enrollment;
import com.beyond.teenkiri.enrollment.dto.EnrollDetResDto;
import com.beyond.teenkiri.enrollment.dto.EnrollListResDto;
import com.beyond.teenkiri.enrollment.dto.EnrollSaveReqDto;
import com.beyond.teenkiri.enrollment.dto.EnrollUpdateReqDto;
import com.beyond.teenkiri.enrollment.repository.EnrollmentRepository;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.lecture.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final LectureService lectureService;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, LectureService lectureService) {
        this.enrollmentRepository = enrollmentRepository;
        this.lectureService = lectureService;
    }

    public Page<EnrollListResDto> enrollList(Pageable pageable){
        Page<Enrollment> enrollments = enrollmentRepository.findAll(pageable);
        Page<EnrollListResDto> enrollListResDto = enrollments.map(a->a.fromListEntity());
        return enrollListResDto;
    }

    public Page<EnrollListResDto> enrollListByGroup(Long subjectId, Pageable pageable){
        Page<Enrollment> enrollments = enrollmentRepository.findAllBySubjectId(subjectId, pageable);
        Page<EnrollListResDto> enrollListResDto = enrollments.map(a->a.fromListEntity());
        return enrollListResDto;
    }

    public EnrollDetResDto enrollDetail(Long id){
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("없는 진행률 입니다."));
        EnrollDetResDto enrollDetResDto = enrollment.fromDetEntity();
        return enrollDetResDto;
    }

    public Enrollment enrollCreate(EnrollSaveReqDto dto){
        Lecture lecture = lectureService.findLectureById(dto.getLectureId());
        Enrollment enrollment = dto.toEntity(lecture);
        enrollmentRepository.save(enrollment);
        return enrollment;
    }

    public Enrollment enrollUpdate(EnrollUpdateReqDto dto){
        Enrollment enrollment = enrollmentRepository.findById(dto.getId()).orElseThrow(()-> new EntityNotFoundException("없는 진행률 입니다."));
        enrollment.update(dto);
        enrollmentRepository.save(enrollment);
        return enrollment;
    }
}
