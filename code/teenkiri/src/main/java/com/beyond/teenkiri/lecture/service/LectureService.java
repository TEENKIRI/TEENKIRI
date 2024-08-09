package com.beyond.teenkiri.lecture.service;

import com.beyond.teenkiri.common.service.CommonMethod;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.common.service.UploadAwsFileService;
import com.beyond.teenkiri.enrollment.domain.Enrollment;
import com.beyond.teenkiri.enrollment.dto.EnrollSaveReqDto;
import com.beyond.teenkiri.enrollment.service.EnrollmentService;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.lecture.dto.*;
import com.beyond.teenkiri.lecture.repository.LectureRepository;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.service.SubjectService;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.UserSubject;
import com.beyond.teenkiri.user.repository.UserSubjectRepository;
import com.beyond.teenkiri.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;


@Service
@Transactional
public class LectureService {
    private final LectureRepository lectureRepository;
    private final SubjectService subjectService;
    private final CommonMethod commonMethod;
    private final UploadAwsFileService uploadAwsFileService;
    private final EnrollmentService enrollmentService;
//    private final EnrollmentRepository enrollmentRepository;
    private final UserService userService;
    private final UserSubjectRepository userSubjectRepository;

    @Autowired
    public LectureService(LectureRepository lectureRepository, SubjectService subjectService, CommonMethod commonMethod, UploadAwsFileService uploadAwsFileService, EnrollmentService enrollmentService, UserService userService, UserSubjectRepository userSubjectRepository) {
        this.lectureRepository = lectureRepository;
        this.subjectService = subjectService;
        this.commonMethod = commonMethod;
        this.uploadAwsFileService = uploadAwsFileService;
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.userSubjectRepository = userSubjectRepository;
    }

    //    강의 리스트 페이지
    public Page<LectureListResDto> lectureList(Pageable pageable){
        Page<Lecture> lectures = lectureRepository.findBydelYN(DelYN.N, pageable);
        Page<LectureListResDto> lectureListResDtos = lectures.map(a->a.fromListEntity());
        return lectureListResDtos;
    }

    //    강의 ((((강좌 그룹별)))) 리스트 페이지
    public Page<LectureListResDto> lectureListByGroup(Long subjectId, Pageable pageable){
        Subject subject = subjectService.findSubjectById(subjectId);
        Page<Lecture> lectures = lectureRepository.findAllBySubjectId(subject.getId() ,pageable);
        Page<LectureListResDto> lectureListResDtos = lectures.map(a->a.fromListEntity());
        return lectureListResDtos;
    }

    //    강의 상세 페이지
    public LectureDetResDto lectureDetail(Long id){
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 강의입니다."));
        LectureDetResDto lectureDetResDto = lecture.fromDetEntity();
        return lectureDetResDto;
    }

//    유저별 강의 수강용 상세 페이지
    public LectureDetPerUserResDto lectureDetailPerUser(Long id){
        System.out.println(11111);
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 강의입니다."));
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);
        Subject subject = lecture.getSubject();

        UserSubject userSubject = userSubjectRepository.findBySubjectIdAndUserId(subject.getId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("수강신청하지 않은 강좌입니다."));

        System.out.println(2222);
        Enrollment enrollment = enrollmentService.findByLectureIdAndUserId(user,lecture);
        LectureDetPerUserResDto lectureDetPerUserResDto;
        if(enrollment == null){
            // 처음 접속한 강의, 진행률 데이터 추가
            EnrollSaveReqDto enrollSaveReqDto = EnrollSaveReqDto.builder()
                    .lectureId(lecture.getId())
                    .userEmail(userEmail)
                    .build();
            Enrollment newEnrollment = enrollmentService.enrollCreate(enrollSaveReqDto);
            lectureDetPerUserResDto = lecture.fromDetPerUserEntity(newEnrollment);
        }else{
            lectureDetPerUserResDto = lecture.fromDetPerUserEntity(enrollment);
        }

        System.out.println(lectureDetPerUserResDto);
        return lectureDetPerUserResDto;
    }

    //    강의 생성
    public Lecture lectureCreate(LectureSaveReqDto dto, MultipartFile videoSsr, MultipartFile imageSsr){

        Subject subject = subjectService.findSubjectById(dto.getSubjectId());
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);
        if(user != null && user.getRole() != Role.ADMIN){
            if(user.getRole() == Role.TEACHER && !Objects.equals(subject.getUserTeacher().getId(), user.getId())){
                throw new RuntimeException("연결되지 않은 선생님입니다. 강의를 업로드하실 수 없습니다.");
            }
        }

        MultipartFile image = (imageSsr == null) ? dto.getImage() : imageSsr;
        MultipartFile video = (videoSsr == null) ? dto.getVideo() : videoSsr;


        Lecture lecture;

        lecture = lectureRepository.save(dto.toEntity(subject));

        try {
            MultipartFile imageFile = image;

            if(!imageFile.isEmpty()){
                String bgImagePathFileName = lecture.getId() + "_"  + imageFile.getOriginalFilename();
                byte[] bgImagePathByte =  imageFile.getBytes();
                String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName,bgImagePathByte);
                lecture.updateImagePath(s3ImagePath);
            }

            MultipartFile videoFile = video;
            if(!videoFile.isEmpty()){
                String bgImagePathFileName = lecture.getId() + "_lecture_"  + videoFile.getOriginalFilename();
                byte[] bgImagePathByte =  videoFile.getBytes();
                String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName,bgImagePathByte);
                lecture.updateVideoPath(s3ImagePath);
            }
        }catch (IOException e) {
            throw new RuntimeException("파일 저장 실패");
        }
        return lecture;
//        Path imagePath = commonMethod.fileSave(image, lecture.getId());
//        Path videoPath = commonMethod.fileSave(video, lecture.getId());
//        if(imagePath != null){
//            lecture.updateImagePath(imagePath.toString());
//        }
//        if(videoPath != null){
//            lecture.updateVideoPath(videoPath.toString());
//        }
    }

    //    강의 업데이트
    public Lecture lectureUpdate(LectureUpdateReqDto dto){
        Lecture lecture = lectureRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("없는 강의입니다."));

        MultipartFile image = dto.getImage();
        MultipartFile video = dto.getVideo();
        Path imagePath = commonMethod.fileSave(image, lecture.getId());
        Path videoPath = commonMethod.fileSave(video, lecture.getId());
        String imageUrl = "", videoUrl = "";
        if(imagePath != null){
            imageUrl = imagePath.toString();
        }
        if(videoPath != null){
            videoUrl = videoPath.toString();
        }
        lecture.toUpdate(dto, videoUrl ,imageUrl);
        lectureRepository.save(lecture);
        return lecture;
    }

    public Lecture lectureDelete(Long id){
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 강의입니다."));
        lecture.toDeleteUpdate();
        return lecture;
    }

    public Long lectureDeleteDeep(Long id){
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 강의입니다."));
        lectureRepository.deleteById(lecture.getId());
        return id;
    }




//    ====================

    public Lecture findLectureById(Long id){
        return lectureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 강의입니다."));
    }





}
