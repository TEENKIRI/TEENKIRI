package com.beyond.teenkiri.user.Handler;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.beyond.teenkiri.user.config.JwtTokenprovider;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.beyond.teenkiri.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.naming.Name;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

        //for naver
//        Object responseObject = oAuth2User.getAttributes().get("response");
//        Map<String, Object> responseMap = (Map<String, Object>) responseObject;
//        String naverEmail = (String) responseMap.get("email");

//        System.out.println("responseObject : " + responseObject);
//        System.out.println("email : " + naverEmail);

        if (email == null) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
                email = (String) kakaoAccount.get("email");
            }
            if (email == null) {
                Object naverObject = oAuth2User.getAttributes().get("response");
                Map<String, Object> naverAccount = (Map<String, Object>) naverObject;
                if (naverAccount != null && naverAccount.containsKey("email")) {
                    email = (String) naverAccount.get("email");
                }
            }
        }

        System.out.println("찾아@!!!!!!!!!!!!!!!!!!!!");
        System.out.println(email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않습니다."));
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
