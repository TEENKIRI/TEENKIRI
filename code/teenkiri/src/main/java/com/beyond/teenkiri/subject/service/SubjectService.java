package com.beyond.teenkiri.subject.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.common.service.UploadAwsFileService;
import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.course.repository.CourseRepository;
import com.beyond.teenkiri.course.service.CourseService;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.dto.SubjectDetResDto;
import com.beyond.teenkiri.subject.dto.SubjectListResDto;
import com.beyond.teenkiri.subject.dto.SubjectSaveReqDto;
import com.beyond.teenkiri.subject.dto.SubjectUpdateReqDto;
import com.beyond.teenkiri.subject.repository.SubjectRepository;
import com.beyond.teenkiri.user.service.UserService;
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
    private final CourseService courseService;
    private final SubjectRepository subjectRepository;
    private final UploadAwsFileService uploadAwsFileService;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, UserService userService
            , CourseService courseService, UploadAwsFileService uploadAwsFileService) {
        this.subjectRepository = subjectRepository;
        this.userService = userService;
        this.courseService = courseService;
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
//        연결된 과목 찾기
        Course course = courseService.findByIdRequired(dto.getCourseId());

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
    public Long subjectUpdate(Long id, SubjectUpdateReqDto dto, MultipartFile subjectThum){
        Subject subject = findSubjectById(id); // 강좌찾기

//        연결 할 정보들 유무 검색
        User user = userService.findByEmail(dto.getUserTeacherEmail());
        Course course = courseService.findByIdRequired(dto.getCourseId());

        try {
            MultipartFile imageFile = subjectThum;
            if(!imageFile.isEmpty()){
                String bgImagePathFileName = subject.getId() + "_"  + imageFile.getOriginalFilename();
                byte[] bgImagePathByte =  imageFile.getBytes();
                String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName,bgImagePathByte);
                subject.updateImagePath(s3ImagePath);
            }
        }catch (IOException e) {
            throw new RuntimeException("파일 저장 실패");
        }

        subject.toUpdate(dto, user, course);
        return subject.getId();
    }


    public Long subjectDelete(Long id){
        Subject subject = subjectRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("존재하지 않는 강좌입니다."));
        subject.updateDelYn(DelYN.Y);
        return subject.getId();
    }

    //    강좌 삭제 및 DB 저장
    public Long subjectDeleteDeep(Long id){
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 강의입니다."));


        if (subject.getLectures().isEmpty()) { // 연결되어있는 강의가 없는 경우
            subjectRepository.deleteById(subject.getId());
            return id;
        } else { // 연결되어있는 강의가 존재하는 경우
            throw new RuntimeException("연결되어있는 강의가 존재하여 삭제하실 수 없습니다.");
        }
    }



    //    ====================
    public Subject findSubjectById(Long id){
        return subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 강좌입니다."));
    }
}