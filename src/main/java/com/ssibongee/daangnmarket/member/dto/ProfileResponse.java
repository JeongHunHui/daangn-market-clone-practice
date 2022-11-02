package com.ssibongee.daangnmarket.member.dto;

import com.ssibongee.daangnmarket.member.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
// 회원 프로필 정보를 응답으로 제공하기 위한 DTO 클래스
// 굳이 DTO를 쓰는 이유가 뭘까?
// 1. 엔티티 내부구현의 캡슐화
// 2. 화면에 필요한 데이터를 선별 가능
// 3. 유효성 검사 코드와 모델링 코드 분리가능
public class ProfileResponse {

    private final String email;
    private final String nickname;

    public static ProfileResponse of(Member member) {
        return ProfileResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
