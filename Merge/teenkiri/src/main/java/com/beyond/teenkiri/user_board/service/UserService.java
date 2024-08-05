package com.beyond.teenkiri.user_board.service;

import com.beyond.teenkiri.user_board.domain.user;
import com.beyond.teenkiri.user_board.dto.UserDetailDto;
import com.beyond.teenkiri.user_board.dto.UserListResDto;
import com.beyond.teenkiri.user_board.dto.UserSaveReqDto;
import com.beyond.teenkiri.user_board.dto.UserUpdateDto;
import com.beyond.teenkiri.user_board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service("userBoardService")
@Transactional(readOnly = true)
public class UserService {
    @Qualifier("userBoardRepository")
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 생성
    @Transactional
    public user userCreate(UserSaveReqDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        user user = dto.toEntity();
        // cascade persist 테스트, remove 테스트는 회원삭제도 대체
        com.beyond.teenkiri.user_board.domain.user savedUser = userRepository.save(user);
        return savedUser;
    }

    // 사용자 리스트
    public Page<UserListResDto> userList(Pageable pageable) {
        Page<user> users = userRepository.findAll(pageable);
        Page<UserListResDto> userListResDtos = users.map(a -> a.listFromEntity());
        return userListResDtos;
    }

    public UserDetailDto userDetail(Long id) {
        user user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        System.out.println(user.detailFromEntity());
        return user.detailFromEntity();
    }

    public user findByEmail(String email) {
        user user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일의 사용자는 없습니다."));
        return user;
    }

    @Transactional
    public void userUpdate(Long id, UserUpdateDto dto) {
        user user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User is not found"));
        user.toUpdate(dto);
    }

    @Transactional
    public void userDelete(Long id) {
        userRepository.deleteById(id);
    }
}
