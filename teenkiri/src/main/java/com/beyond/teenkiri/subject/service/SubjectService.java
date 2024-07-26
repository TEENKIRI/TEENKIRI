package com.beyond.teenkiri.subject.service;

import com.beyond.teenkiri.subject.dto.SubjectSaveReqDto;
import com.beyond.teenkiri.subject.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubjectService {
    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }


//    강좌 생성 및 DB 저장
    public Long subjectCreate(SubjectSaveReqDto dto){


        return null; // save된 subject의 id return;
    }

}
