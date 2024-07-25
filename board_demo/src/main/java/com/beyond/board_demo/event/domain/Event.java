package com.beyond.board_demo.event.domain;

import com.beyond.board_demo.common.domain.BaseTimeEntity;
import com.beyond.board_demo.common.domain.DelYN;
import com.beyond.board_demo.event.dto.EventDetailDto;
import com.beyond.board_demo.event.dto.EventListResDto;
import com.beyond.board_demo.event.dto.EventUpdateDto;
import com.beyond.board_demo.user.domain.User;
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
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('N', 'Y') DEFAULT 'N'")
    private DelYN delYN;

    public EventDetailDto fromDetailEntity() {
        return EventDetailDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .content(this.getContent())
                .userNickname(this.getUser().getNickname())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public EventListResDto listFromEntity() {
        return EventListResDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .userNickname(this.getUser().getNickname())
                .createdTime(this.getCreatedTime())
                .build();
    }

    public void toUpdate(EventUpdateDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();

    }

}
