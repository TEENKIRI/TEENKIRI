package com.beyond.teenkiri.subject.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.course.repository.CourseRepository;
import com.beyond.teenkiri.user_board.domain.Role;
import com.beyond.teenkiri.user_board.domain.User;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.dto.SubjectDetResDto;
import com.beyond.teenkiri.subject.dto.SubjectListResDto;
import com.beyond.teenkiri.subject.dto.SubjectSaveReqDto;
import com.beyond.teenkiri.subject.dto.SubjectUpdateReqDto;
import com.beyond.teenkiri.subject.repository.SubjectRepository;
import com.beyond.teenkiri.user_board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class SubjectService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, UserRepository userRepository
            , CourseRepository courseRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }


//    강좌 list
    public Page<SubjectListResDto> subjectList(Pageable pageable){
//        Page<Subject> subject = subjectRepository.findAll(pageable);
        Page<Subject> subject = subjectRepository.findBydelYN(DelYN.N, pageable);
        Page<SubjectListResDto> subjectListResDtos = subject.map(a->a.fromListEntity());
        return subjectListResDtos;
    }


//    강좌 순위별 list
    public Page<SubjectListResDto> subjectRatingList(Pageable pageable){
        Page<Subject> subject = subjectRepository.findAllBydelYNOrderByRatingDesc(DelYN.N, pageable);
        Page<SubjectListResDto> subjectListResDtos = subject.map(a->a.fromListEntity());
        return subjectListResDtos;
    }


//    강좌 상세
    public SubjectDetResDto subjectDetail(Long id){
//        🚨추후 멤버.. 추가되면 권한체크 + 멤버 연결 체크
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 강좌 입니다."));
        SubjectDetResDto subjectDetResDto = subject.fromDetEntity();

        return subjectDetResDto;
    }


//    강좌 생성 및 DB 저장
    public Subject subjectCreate(SubjectSaveReqDto dto){
//        🚨추후 멤버..
        User user = userRepository.findById(dto.getUserId()).orElseThrow(()-> new EntityNotFoundException("없는 유저입니다."));

        if(!user.getRole().equals(Role.ADMIN)){ // 관리자 레벨만 강좌를 생성할 수 있도록 권한설정
            throw new IllegalArgumentException("권한이 부족합니다.");
        }

        Course course = courseRepository.findById(dto.getCourseId()).orElseThrow(()-> new EntityNotFoundException("없는 과목 입니다."));

        Subject subject = dto.toEntity(user,course);
        subjectRepository.save(subject);

        return subject; // save된 subject return;
    }

//    강좌 업데이트 및 DB 저장
    public Long subjectUpdate(SubjectUpdateReqDto dto){


        return null;
    }

//    강좌 삭제 및 DB 저장
    public Long subjectDelete(Long id){
        return null;
    }



//    ====================
    public Subject findSubjectById(Long id){
        return subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 강좌입니다."));
    }
}
