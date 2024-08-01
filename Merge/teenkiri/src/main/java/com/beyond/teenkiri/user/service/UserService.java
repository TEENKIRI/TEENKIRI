package com.beyond.teenkiri.user.service;

import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.dto.UserDetailDto;
import com.beyond.teenkiri.user.dto.UserListResDto;
import com.beyond.teenkiri.user.dto.UserSaveReqDto;
import com.beyond.teenkiri.user.dto.UserUpdateDto;
import com.beyond.teenkiri.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 생성
    @Transactional
    public User userCreate(UserSaveReqDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        User user = dto.toEntity();
        // cascade persist 테스트, remove 테스트는 회원삭제도 대체
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    // 사용자 리스트
    public Page<UserListResDto> userList(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserListResDto> userListResDtos = users.map(a -> a.listFromEntity());
        return userListResDtos;
    }

    public UserDetailDto userDetail(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        System.out.println(user.detailFromEntity());
        return user.detailFromEntity();
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일의 사용자는 없습니다."));
        return user;
    }

    @Transactional
    public void userUpdate(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User is not found"));
        user.toUpdate(dto);
    }

    @Transactional
    public void userDelete(Long id) {
        userRepository.deleteById(id);
    }
}
