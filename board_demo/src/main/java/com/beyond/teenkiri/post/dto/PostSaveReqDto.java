package com.beyond.teenkiri.post.dto;



import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.user.domain.User;
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
    private String email;

    public Post toEntity(User user){
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                .user(user)
                .build();
    }
}
