package com.ssibongee.daangnmarket.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Builder
@Getter
@RequiredArgsConstructor
// 사용자 프로필 정보 변경요청을 위한 클래스
public class ProfileRequest {

    private final String email;
    private final String nickname;

}
