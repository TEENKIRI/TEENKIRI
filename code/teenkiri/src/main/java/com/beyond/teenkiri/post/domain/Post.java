package com.beyond.teenkiri.post.domain;

import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.dto.PostDetailDto;
import com.beyond.teenkiri.post.dto.PostListResDto;
import com.beyond.teenkiri.post.dto.PostUpdateDto;
import com.beyond.teenkiri.user.domain.User;
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

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String imageUrl = "";

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public PostListResDto listFromEntity() {
        return PostListResDto.builder()
                .id(this.id)
                .title(this.title)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .imageUrl(this.imageUrl)
                .nickname(this.user.getNickname())
                .user_id(this.user.getId())
                .build();
    }

    public PostDetailDto detailFromEntity() {
        return PostDetailDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .content(this.getContent())
                .nickname(this.user.getNickname())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .user_id(this.user.getId())
                .build();
    }

    public void toUpdate(PostUpdateDto dto,  String imageUrl) {
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
