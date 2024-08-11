package com.beyond.teenkiri.user.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.dto.SubjectInfoDto;
import com.beyond.teenkiri.user.dto.UserSubjectListResDto;
import com.beyond.teenkiri.wish.domain.Wish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSubject extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;


//    private List<Subject> subjects;


    public UserSubjectListResDto listFromEntity(List<SubjectInfoDto> subjects, int subjectCount) {
        return UserSubjectListResDto.builder()
                .subjects(subjects)
                .subjectCount(subjectCount)
                .build();
    }

}
