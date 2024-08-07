package com.beyond.teenkiri.subject.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.common.service.UploadAwsFileService;
import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.course.repository.CourseRepository;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.dto.SubjectDetResDto;
import com.beyond.teenkiri.subject.dto.SubjectListResDto;
import com.beyond.teenkiri.subject.dto.SubjectSaveReqDto;
import com.beyond.teenkiri.subject.dto.SubjectUpdateReqDto;
import com.beyond.teenkiri.subject.repository.SubjectRepository;
import com.beyond.teenkiri.user.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@Transactional
public class SubjectService {
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final UploadAwsFileService uploadAwsFileService;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, UserService userService
            , CourseRepository courseRepository, UploadAwsFileService uploadAwsFileService) {
        this.subjectRepository = subjectRepository;
        this.userService = userService;
        this.courseRepository = courseRepository;
        this.uploadAwsFileService = uploadAwsFileService;
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
    public Subject subjectCreate(SubjectSaveReqDto dto,MultipartFile subjectThum){
//        로그인 된 선생님 이메일 추가하기
        User user = userService.findByEmail(dto.getUserTeacherEmail());
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(user);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        if(user == null || user.getId().equals("") ){
            throw new EntityNotFoundException("존재하지 않는 선생님 입니다."); // ?
        }
        Course course = courseRepository.findById(dto.getCourseId()).orElseThrow(()-> new EntityNotFoundException("없는 과목 입니다."));
        Subject subject = dto.toEntity(user,course);

        subjectRepository.save(subject);
        try{
            MultipartFile image = subjectThum;
            if(!image.isEmpty()){
                String bgImagePathFileName = course.getId() + "_"  + image.getOriginalFilename();
                byte[] bgImagePathByte =  image.getBytes();
                String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName,bgImagePathByte);
                subject.updateImagePath(s3ImagePath);
            }
        }catch (IOException e) {
            throw new RuntimeException("파일 저장 실패");
        }

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