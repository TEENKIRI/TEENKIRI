package com.beyond.teenkiri.post.dto;



import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveReqDto {
    private Long user_id;
    private String title;
    private String content;
    private String userEmail;
    private String nickname;

    @Builder.Default
    private DelYN delYN = DelYN.N;
    private User user;
    private MultipartFile image;

    public Post toEntity(){
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .delYN(this.delYN)
                .user(this.user)
                .build();
    }
}
