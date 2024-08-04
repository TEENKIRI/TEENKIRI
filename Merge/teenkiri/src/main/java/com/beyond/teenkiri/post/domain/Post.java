package com.beyond.teenkiri.post.domain;

import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.dto.PostDetailDto;
import com.beyond.teenkiri.post.dto.PostListResDto;
import com.beyond.teenkiri.post.dto.PostUpdateDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private com.beyond.teenkiri.user_board.domain.user user;

    @Enumerated(EnumType.STRING)
    private DelYN delYN;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public PostListResDto listFromEntity() {
        return PostListResDto.builder()
                .id(this.id)
                .title(this.title)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .user_email(this.user.getEmail())
                .build();
    }

    public PostDetailDto detailFromEntity() {
        return PostDetailDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .contents(this.getContents())
                .userEmail(this.user.getEmail())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public void toUpdate(PostUpdateDto dto) {
        this.title = dto.getTitle();
        this.contents = dto.getContents();
    }

    public void updateDelYN(DelYN delYN){
        this.delYN = delYN;
    }
}
