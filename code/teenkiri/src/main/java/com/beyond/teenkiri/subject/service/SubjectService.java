package com.beyond.teenkiri.subject.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.common.service.UploadAwsFileService;
import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.course.service.CourseService;
import com.beyond.teenkiri.subject.domain.Grade;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.dto.SubjectDetResDto;
import com.beyond.teenkiri.subject.dto.SubjectListResDto;
import com.beyond.teenkiri.subject.dto.SubjectSaveReqDto;
import com.beyond.teenkiri.subject.dto.SubjectUpdateReqDto;
import com.beyond.teenkiri.subject.repository.SubjectRepository;
import com.beyond.teenkiri.user.domain.UserSubject;
import com.beyond.teenkiri.user.repository.UserSubjectRepository;
import com.beyond.teenkiri.user.service.UserService;
import com.beyond.teenkiri.wish.domain.Wish;
import com.beyond.teenkiri.wish.service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class SubjectService {
    private final UserService userService;
    private final CourseService courseService;
    private final SubjectRepository subjectRepository;
    private final UploadAwsFileService uploadAwsFileService;
    private final UserSubjectRepository userSubjectRepository;
    private final WishService wishService;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, UserService userService
            , CourseService courseService, UploadAwsFileService uploadAwsFileService, UserSubjectRepository userSubjectRepository, WishService wishService) {
        this.subjectRepository = subjectRepository;
        this.userService = userService;
        this.courseService = courseService;
        this.uploadAwsFileService = uploadAwsFileService;
        this.userSubjectRepository = userSubjectRepository;
        this.wishService = wishService;
    }


    //    강좌 list
//    public Page<SubjectListResDto> subjectList(Pageable pageable){
////        Page<Subject> subject = subjectRepository.findAll(pageable);
//        Page<Subject> subject = subjectRepository.findBydelYN(DelYN.N, pageable);
//        Page<SubjectListResDto> subjectListResDtos = subject.map(a->a.fromListEntity());
//        return subjectListResDtos;
//    }


    // 강좌 list(검색 기능 추가)

    public Page<SubjectListResDto> subjectList(Pageable pageable, String search, String searchType, String sortType, String grades, Long courseId) {
        Page<Subject> subjects;

        List<Grade> gradesList = (grades != null && !grades.isEmpty()) ?
                Arrays.stream(grades.split("&"))
                        .map(Grade::valueOf)
                        .collect(Collectors.toList()) : null;

        pageable = applySorting(pageable, sortType);

        if (courseId != null) {
            if (search == null || search.isEmpty()) {
                subjects = gradesList != null && !gradesList.isEmpty() ?
                        subjectRepository.findByCourseIdAndGradeInAndDelYN(courseId, gradesList, DelYN.N, pageable) :
                        subjectRepository.findByCourseIdAndDelYN(courseId, DelYN.N, pageable);
            } else {
                switch (searchType) {
                    case "title":
                        subjects = gradesList != null && !gradesList.isEmpty() ?
                                subjectRepository.findByCourseIdAndTitleContainingAndGradeInAndDelYN(courseId, search, gradesList, DelYN.N, pageable) :
                                subjectRepository.findByCourseIdAndTitleContainingAndDelYN(courseId, search, DelYN.N, pageable);
                        break;
                    case "userTeacher":
                        subjects = gradesList != null && !gradesList.isEmpty() ?
                                subjectRepository.findByCourseIdAndUserTeacherNameContainingAndGradeInAndDelYN(courseId, search, gradesList, DelYN.N, pageable) :
                                subjectRepository.findByCourseIdAndUserTeacherNameContainingAndDelYN(courseId, search, DelYN.N, pageable);
                        break;
                    case "all":
                        subjects = gradesList != null && !gradesList.isEmpty() ?
                                subjectRepository.findByCourseIdAndTitleContainingOrUserTeacherNameContainingAndGradeInAndDelYN(courseId, search, search, gradesList, DelYN.N, pageable) :
                                subjectRepository.findByCourseIdAndTitleContainingOrUserTeacherNameContainingAndDelYN(courseId, search, search, DelYN.N, pageable);
                        break;
                    default:
                        subjects = gradesList != null && !gradesList.isEmpty() ?
                                subjectRepository.findByCourseIdAndGradeInAndDelYN(courseId, gradesList, DelYN.N, pageable) :
                                subjectRepository.findByCourseIdAndDelYN(courseId, DelYN.N, pageable);
                        break;
                }
            }
        } else {
            // 기존 로직 사용 (courseId 없는 경우)
            if (search == null || search.isEmpty()) {
                if (gradesList != null && !gradesList.isEmpty()) {
                    subjects = subjectRepository.findByGradeInAndDelYN(gradesList, DelYN.N, pageable);
                } else {
                    subjects = subjectRepository.findByDelYN(DelYN.N, pageable);
                }
            } else {
                switch (searchType) {
                    case "title":
                        subjects = gradesList != null && !gradesList.isEmpty() ?
                                subjectRepository.findByTitleContainingAndGradeInAndDelYN(search, gradesList, DelYN.N, pageable) :
                                subjectRepository.findByTitleContainingAndDelYN(search, DelYN.N, pageable);
                        break;
                    case "userTeacher":
                        subjects = gradesList != null && !gradesList.isEmpty() ?
                                subjectRepository.findByUserTeacherNameContainingAndGradeInAndDelYN(search, gradesList, DelYN.N, pageable) :
                                subjectRepository.findByUserTeacherNameContainingAndDelYN(search, DelYN.N, pageable);
                        break;
                    case "all":
                        subjects = gradesList != null && !gradesList.isEmpty() ?
                                subjectRepository.findByTitleContainingOrUserTeacherNameContainingAndGradeInAndDelYN(search, search, gradesList, DelYN.N, pageable) :
                                subjectRepository.findByTitleContainingOrUserTeacherNameContainingAndDelYN(search, search, DelYN.N, pageable);
                        break;
                    default:
                        subjects = gradesList != null && !gradesList.isEmpty() ?
                                subjectRepository.findByGradeInAndDelYN(gradesList, DelYN.N, pageable) :
                                subjectRepository.findByDelYN(DelYN.N, pageable);
                        break;
                }
            }
        }

//        로그인 여부 확인 및 찜 확인
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmailReturnNull(userEmail);
        return subjects.map(a-> {
            Wish wish = null;
                if(user != null){
                    wish = wishService.findBySubjectIdAndUserIdReturnNull(a, user);
                }
            return a.fromListEntity(wish);
        });
    }

    private Pageable applySorting(Pageable pageable, String sortType) {
        if ("like".equals(sortType)) {
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "rating"));
        } else {
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdTime"));
        }
    }

    //    강좌 과목별 list
    public Page<SubjectListResDto> subjectPerCourseList(Pageable pageable, Long courseId){
        Course course = courseService.findByIdRequired(courseId);
        Page<Subject> subject = subjectRepository.findByCourseIdAndDelYN(course.getId(), DelYN.N, pageable);

//        로그인 여부 확인 및 찜 확인
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmailReturnNull(userEmail);

        Page<SubjectListResDto> subjectListResDtos = subject.map(a-> {
            Wish wish = null;
            if(user != null){
                wish = wishService.findBySubjectIdAndUserIdReturnNull(a, user);
            }
            return a.fromListEntity(wish);
        });
        return subjectListResDtos;
    }

    //    강좌 과목 및 학년별 list
    public Page<SubjectListResDto> subjectPerCourseAndGradeList(Pageable pageable, Long courseId, String grades){
        Course course = courseService.findByIdRequired(courseId);
        List<Grade> gradesArr = Arrays.stream(grades.split("&")).map(Grade::valueOf).collect(Collectors.toList());
        Page<Subject> subject = subjectRepository.findByCourseIdAndGradeInAndDelYN(course.getId(), gradesArr, DelYN.N, pageable);

        //        로그인 여부 확인 및 찜 확인
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmailReturnNull(userEmail);
        Page<SubjectListResDto> subjectListResDtos = subject.map(a-> {
            Wish wish = null;
            if(user != null){
                wish = wishService.findBySubjectIdAndUserIdReturnNull(a, user);
            }
            return a.fromListEntity(wish);
        });
        return subjectListResDtos;
    }

    //    강좌 순위별 list
    public Page<SubjectListResDto> subjectRatingList(Pageable pageable) {
        Page<Subject> subject = subjectRepository.findAllByDelYNOrderByRatingDesc(DelYN.N, pageable);

        //        로그인 여부 확인 및 찜 확인
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmailReturnNull(userEmail);

        Page<SubjectListResDto> subjectListResDtos = subject.map(a-> {
            Wish wish = null;
            if(user != null){
                wish = wishService.findBySubjectIdAndUserIdReturnNull(a, user);
            }
            return a.fromListEntity(wish);
        });
        return subjectListResDtos;
    }


    //    강좌 상단 노출용 표시된 list
    public Page<SubjectListResDto> subjectMainList(Pageable pageable){
        Boolean isMainsubject = true; // 상단 표시된 강좌 리스트만
        Page<Subject> subject = subjectRepository.findByIsMainSubjectAndDelYN(isMainsubject, DelYN.N, pageable);
        //        로그인 여부 확인 및 찜 확인
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmailReturnNull(userEmail);

        Page<SubjectListResDto> subjectListResDtos = subject.map(a-> {
            Wish wish = null;
            if(user != null){
                wish = wishService.findBySubjectIdAndUserIdReturnNull(a, user);
            }
            return a.fromListEntity(wish);
        });
        return subjectListResDtos;
    }


    //    강좌 상세
    public SubjectDetResDto subjectDetail(Long id){
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 강좌 입니다."));
//        🚨추후 멤버.. 추가되면 권한체크 + 멤버 연결 체크
        User user = null;
        UserSubject userSubject = null;
        Wish wish = null;
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userEmail.isEmpty()){
            user = userService.findByEmailReturnNull(userEmail);
            if(user != null){
                userSubject = userSubjectRepository.findBySubjectIdAndUserId(subject.getId(), user.getId()).orElse(null);
                wish = wishService.findBySubjectIdAndUserIdReturnNull(subject, user);
            }
        }
        SubjectDetResDto subjectDetResDto = subject.fromDetEntity(userSubject, wish);

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

        subject.toUpdateIsMainSubject(dto.getIsMainSubject());

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

        subject.toUpdateIsMainSubject(dto.getIsMainSubject());

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