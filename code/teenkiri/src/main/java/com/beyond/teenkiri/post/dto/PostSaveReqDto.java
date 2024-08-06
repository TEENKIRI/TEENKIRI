package com.beyond.teenkiri.post.dto;



import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.user_board.domain.user;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveReqDto {
    private String title;
    private String contents;
    private String userEmail;
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public Post toEntity(user user){
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                .delYN(this.delYN)
                .user(user)
                .build();
    }
}
