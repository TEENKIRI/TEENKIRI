package com.beyond.board_demo.post.domain;

import com.beyond.board_demo.comment.domain.Comment;
import com.beyond.board_demo.common.BaseTimeEntity;
import com.beyond.board_demo.common.DelYN;
import com.beyond.board_demo.post.dto.PostDetailDto;
import com.beyond.board_demo.post.dto.PostListResDto;
import com.beyond.board_demo.post.dto.PostUpdateDto;
import com.beyond.board_demo.user.domain.User;
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
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('N', 'Y') DEFAULT 'N'")
    private DelYN delYN;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public PostListResDto listFromEntity() {
        return PostListResDto.builder()
                .id(this.id)
                .title(this.title)
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
}
