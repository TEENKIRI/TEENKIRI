package com.beyond.teenkiri.notice.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.notice.dto.NoticeDetailDto;
import com.beyond.teenkiri.notice.dto.NoticeListResDto;
import com.beyond.teenkiri.notice.dto.NoticeUpdateDto;
import com.beyond.teenkiri.user.domain.User;
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


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public NoticeDetailDto fromDetailEntity() {
        return NoticeDetailDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .content(this.getContent())
                .nickname(this.getUser().getNickname())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public  NoticeListResDto listFromEntity() {
        return NoticeListResDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .nickname(this.getUser().getNickname())
                .createdTime(this.getCreatedTime())
                .build();
    }

    public void toUpdate(NoticeUpdateDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();

    }

}
