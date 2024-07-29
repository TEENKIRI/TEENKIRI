package com.beyond.teenkiri.lecture.service;

import com.beyond.teenkiri.common.CommonMethod;
import com.beyond.teenkiri.common.DelYN;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.lecture.dto.LectureDetResDto;
import com.beyond.teenkiri.lecture.dto.LectureListResDto;
import com.beyond.teenkiri.lecture.dto.LectureSaveReqDto;
import com.beyond.teenkiri.lecture.dto.LectureUpdateReqDto;
import com.beyond.teenkiri.lecture.repository.LectureRepository;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


@Service
@Transactional
public class LectureService {
    private final LectureRepository lectureRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public LectureService(LectureRepository lectureRepository, SubjectRepository subjectRepository) {
        this.lectureRepository = lectureRepository;
        this.subjectRepository = subjectRepository;
    }

    //    강의 리스트 페이지
    public Page<LectureListResDto> lectureList(Pageable pageable){
        Page<Lecture> lectures = lectureRepository.findBydelYN(DelYN.N, pageable);
        Page<LectureListResDto> lectureListResDtos = lectures.map(a->a.fromEntity());
        return null;
    }

    //    강의 ((((강좌 그룹별)))) 리스트 페이지
    public Page<LectureListResDto> lectureListByGroup(Pageable pageable){
        return null;
    }

    //    강의 상세 페이지
    public LectureDetResDto lectureDetail(Long id){
        return null;
    }

    //    강의 생성
    public Lecture lectureCreate(LectureSaveReqDto dto){

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("없는 강좌입니다."));

        MultipartFile image = dto.getImage();
        MultipartFile video = dto.getVideo();

        // 🚨 파일 확장자 체크 필요
        if(image != null){
            Boolean imageBoolean = CommonMethod.fileSizeCheck(image);
            if(Boolean.FALSE.equals(imageBoolean)){
                throw new IllegalArgumentException("image 파일의 크기가 너무 큽니다.");
            }
        }

        if(video != null){
            Boolean videoBoolean = CommonMethod.fileSizeCheck(video);
            if(Boolean.FALSE.equals(videoBoolean)){
                throw new IllegalArgumentException("video 파일의 크기가 너무 큽니다.");
            }
        }




        Lecture lecture;
        try{
            lecture = lectureRepository.save(dto.toEntity(subject));
            byte[] imageBytes = image.getBytes();
            Path imagePath = Paths.get("C:/Users/rro06/OneDrive/Desktop/tmp/", lecture.getId() + "_" + image.getOriginalFilename());
            Files.write(imagePath,imageBytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            byte[] videoBytes = video.getBytes();
            Path videoPath = Paths.get("C:/Users/rro06/OneDrive/Desktop/tmp/", lecture.getId() + "_" + video.getOriginalFilename());
            Files.write(videoPath,videoBytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            lecture.updateImagePath(imagePath.toString());
            lecture.updateVideoPath(videoPath.toString());

        }catch (IOException e){
            throw new RuntimeException("강의 저장 실패");
        }
        return lecture;
    }

    //    강의 업데이트
    public Lecture lectureUpdate(LectureUpdateReqDto dto){
        return null;
    }





}
