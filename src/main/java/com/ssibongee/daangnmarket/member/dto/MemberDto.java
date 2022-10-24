package com.ssibongee.daangnmarket.member.dto;

import com.ssibongee.daangnmarket.member.domain.entity.Member;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
// Member의 정보를 담은 DTO
// 데이터 전달 시 유효성 검사를 위해 Member 엔티티와 분리
public class MemberDto {

    // null or 빈 값("")인지 확인하는 유효성 검증 어노테이션
    @NotEmpty
    // 정규표현식에 맞는지 검사, 검증 실패시 message 속성의 값 전달
    @Email(message = "유효하지 않은 이메일 형식입니다.",
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @NotEmpty
    // 정규표현식에 맞는지 검사, 검증 실패시 message 속성의 값 전달
    @Pattern(message = "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!~$%^&-+=()])(?=\\S+$).{8,16}$")
    private String password;

    @NotEmpty
    private String nickname;

    // Member의 builder를 이용해서 MemberDto로 Member 엔티티 생성
    public static Member toEntity(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(memberDto.getEmail())
                // 비밀번호는 암호화하여 저장
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .build();
    }
}
