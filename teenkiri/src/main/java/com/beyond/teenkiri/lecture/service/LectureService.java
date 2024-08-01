package com.beyond.teenkiri.lecture.service;

import com.beyond.teenkiri.common.CommonMethod;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.lecture.dto.LectureDetResDto;
import com.beyond.teenkiri.lecture.dto.LectureListResDto;
import com.beyond.teenkiri.lecture.dto.LectureSaveReqDto;
import com.beyond.teenkiri.lecture.dto.LectureUpdateReqDto;
import com.beyond.teenkiri.lecture.repository.LectureRepository;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.nio.file.Path;


@Service
@Transactional
public class LectureService {
    private final LectureRepository lectureRepository;
    private final SubjectService subjectService;
    private final CommonMethod commonMethod;

    @Autowired
    public LectureService(LectureRepository lectureRepository, SubjectService subjectService, CommonMethod commonMethod) {
        this.lectureRepository = lectureRepository;
        this.subjectService = subjectService;
        this.commonMethod = commonMethod;
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

    //    강의 생성
    public Lecture lectureCreate(LectureSaveReqDto dto){

        Subject subject = subjectService.findSubjectById(dto.getSubjectId());

        MultipartFile image = dto.getImage();
        MultipartFile video = dto.getVideo();

        Lecture lecture;

        lecture = lectureRepository.save(dto.toEntity(subject));
        Path imagePath = commonMethod.fileSave(image, lecture.getId());
        Path videoPath = commonMethod.fileSave(video, lecture.getId());
        if(imagePath != null){
            lecture.updateImagePath(imagePath.toString());
        }
        if(videoPath != null){
            lecture.updateVideoPath(videoPath.toString());
        }

        return lecture;
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
