package com.beyond.teenkiri.User.service;

import com.beyond.teenkiri.User.domain.User;
import com.beyond.teenkiri.User.dto.UserDto;
import com.beyond.teenkiri.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto registerUser(UserDto userDto) {
        try {
            if (userDto.getPassword() == null) {
                throw new IllegalArgumentException("rawPassword cannot be null");
            }
            UserDto encodedUserDto = userDto.withEncodedPassword(passwordEncoder.encode(userDto.getPassword()));
            User user = User.fromEntity(encodedUserDto);
            userRepository.save(user);
            return UserDto.toEntity(user);
        } catch (Exception e) {
            System.err.println("Error during user registration: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public UserDto loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return UserDto.toEntity(user);
    }

    public boolean isEmailDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    public String findUserEmail(String username, String phone) {
        User user = userRepository.findByUsernameAndPhone(username, phone)
                .orElse(null);
        return user != null ? user.getEmail() : null;
    }

    public boolean verifyUser(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email).isPresent();
    }


    public void updatePassword(String email, String newPassword) {
        try {
            User existingUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

            UserDto userDto = UserDto.toEntity(existingUser).withEncodedPassword(passwordEncoder.encode(newPassword));
            User updatedUser = User.fromEntity(userDto);
            userRepository.save(updatedUser);
        } catch (Exception e) {
            System.err.println("비밀번호 업데이트 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}