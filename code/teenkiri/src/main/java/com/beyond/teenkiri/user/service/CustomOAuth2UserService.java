package com.beyond.teenkiri.user.service;


import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserSevice를 통해 가져온 OAuth2User의 attribute를 저장
        OAuthAttributes attributes = OAuthAttributes.of(registrationId,
                userNameAttributeName, oAuth2User.getAttributes());

        User user = null;
        if ("kakao".equals(registrationId)) {
            user = kakaoSaveOrUpdate(attributes);
        }else {
            user = saveOrUpdate(attributes);
        }
        if ("naver".equals(registrationId)) {
            user = naverSaveOrUpdate(attributes);
        }else {
            user = saveOrUpdate(attributes);
        }
        if (user.getName() == null){
            String temp = "이름을 변경해주세요";
            user.updateName(temp);
        }

        if (user.getNickname() == null) {
            String uuidNick = String.valueOf(UUID.randomUUID());
            user.updateNick(uuidNick);
        }
        if (user.getPassword() == null) {
            String uuidPass = String.valueOf(UUID.randomUUID());
            user.updatePass(uuidPass);
        }
        if (user.getAddress() == null){
            String address = "임시주소입니다. 변경해주세요";
            user.updateAddress(address);
        }

        userRepository.save(user);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {

        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }

    private User kakaoSaveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
    private User naverSaveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName()))
                .orElse(attributes.toEntity());
        return  userRepository.save(user);
    }
}