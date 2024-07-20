package com.beyond.board_demo.notice.domain;

import com.beyond.board_demo.common.BaseTimeEntity;
import com.beyond.board_demo.notice.dto.NoticeDetailDto;
import com.beyond.board_demo.notice.dto.NoticeListResDto;
import com.beyond.board_demo.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notice")
public class Notice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 3000, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public NoticeDetailDto fromDetailEntity() {
        return NoticeDetailDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .content(this.getContent())
                .userNickname(this.getUser().getNickname())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public  NoticeListResDto fromListEntity() {
        return NoticeListResDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .userNickname(this.getUser().getNickname())
                .createdTime(this.getCreatedTime())
                .build();
    }
}
