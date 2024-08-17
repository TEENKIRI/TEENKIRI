package com.beyond.teenkiri.user.dto;

import com.beyond.teenkiri.user.domain.UserSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSubjectListResDto {
    private List<SubjectInfoDto> subjects;
    private int subjectCount;
}
