package com.beyond.teenkiri.user.Handler;

import com.beyond.teenkiri.user.config.JwtTokenprovider;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private final JwtTokenprovider jwtTokenprovider;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 구글 회원입니다."));
        Long getUserId = user.getId();
        Long userId = getUserId;

//        String userIdString = oAuth2User.getAttribute("sub");
//        Long userId = userIdString != null ? Long.valueOf(userIdString) : null;


        String token = jwtTokenprovider.googleToken(email, userId,"STUDENT");
        System.out.println("토큰은!!!!" + token);
        // 리다이렉트 URL 설정
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8082/loginSuccess")
                .queryParam("token", token)
                .build().toUriString();

        // 리다이렉트 수행
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
