package com.beyond.teenkiri.user.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.dto.UserDetailDto;
import com.beyond.teenkiri.user.dto.UserListResDto;
import com.beyond.teenkiri.user.dto.UserUpdateDto;
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
public class UserSubject extends BaseTimeEntity { //User가 갖고있는 강좌의 개수
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

}
