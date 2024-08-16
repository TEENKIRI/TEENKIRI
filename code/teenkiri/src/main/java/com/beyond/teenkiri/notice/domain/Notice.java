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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String imageUrl = "";

    public NoticeDetailDto fromDetailEntity() {
        return NoticeDetailDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .content(this.getContent())
                .nickname(this.getUser().getNickname())
                .imageUrl(this.imageUrl)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public  NoticeListResDto listFromEntity() {
        return NoticeListResDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .nickname(this.getUser().getNickname())
                .imageUrl(this.imageUrl)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public void toUpdate(NoticeUpdateDto dto, String imageUrl){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.imageUrl = imageUrl;

    }
    public void updateImagePath(String imagePath){
        this.imageUrl = imagePath;
    }

    public void updateDelYN(DelYN delYN){
        this.delYN = delYN;
    }
}
