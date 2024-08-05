package com.beyond.teenkiri.event.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.event.dto.EventDetailDto;
import com.beyond.teenkiri.event.dto.EventListResDto;
import com.beyond.teenkiri.event.dto.EventUpdateDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "event")
public class Event extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 3000, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private com.beyond.teenkiri.user_board.domain.user user;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public EventDetailDto fromDetailEntity() {
        return EventDetailDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .content(this.getContent())
                .nickname(this.getUser().getNickname())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public EventListResDto listFromEntity() {
        return EventListResDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .nickname(this.getUser().getNickname())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public void toUpdate(EventUpdateDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();

    }

    public void updateDelYN(DelYN delYN){
        this.delYN = delYN;
    }
}