package com.beyond.teenkiri.user.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.dto.UserSubjectListResDto;
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

    public UserSubjectListResDto listFromEntity(List<String> subjectTitles, int subjectCount){
        return UserSubjectListResDto.builder()
                .userEmail(this.user.getEmail())
                .subjectTitles(subjectTitles)
                .subjectCount(subjectCount)
                .build();
    }
}
