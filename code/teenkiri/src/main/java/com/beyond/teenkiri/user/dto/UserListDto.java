package com.beyond.teenkiri.user.dto;

import com.beyond.teenkiri.common.domain.Address;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserListDto {
    private Long id;
    private String name;
    private String phone;
    private Address address;
    private String email;
    private String nickname;
    private Role role;
    private int reportCount;
    private DelYN delYN;
}
