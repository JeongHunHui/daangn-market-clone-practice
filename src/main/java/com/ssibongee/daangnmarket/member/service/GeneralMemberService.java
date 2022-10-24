package com.ssibongee.daangnmarket.member.service;

import com.ssibongee.daangnmarket.member.exception.MemberNotFoundException;
import com.ssibongee.daangnmarket.member.dto.LocationAddressRequest;
import com.ssibongee.daangnmarket.member.dto.MemberDto;
import com.ssibongee.daangnmarket.member.dto.PasswordRequest;
import com.ssibongee.daangnmarket.member.dto.ProfileRequest;
import com.ssibongee.daangnmarket.member.domain.entity.Member;
import com.ssibongee.daangnmarket.member.domain.repository.MemberRepository;
import com.ssibongee.daangnmarket.member.exception.PasswordNotMatchedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// 스프링에게 해당 클래스가 Service라는 것을 알림 -> Bean 으로 등록
@Service
// Lombok, final 이 붙은 필드로만 생성자 생성
@RequiredArgsConstructor
// MemberService를 상속받아서 구현
public class GeneralMemberService implements MemberService {

    // RequiredArgsConstructor로 생성자 생성
    // + 생성자가 하나이므로 @Autowired 생략 -> MemberRepository DI
    private final MemberRepository memberRepository;

    @Override
    // 해당 범위 내의 메서드들이 트랜잭션이 되도록 함
    // Member 등록 중 실패할 경우 결과가 DB에 Commit되지 않는다.
    // readOnly 속성으로 읽기 전용 트랜잭션으로 설정 가능
    @Transactional
    public void registrationMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public Member findMemberByEmail(String email) {
        // findMemberByEmail 메서드의 반환 타입이 Optional
        // orElseThrow = Optional객체가 비어있다면 에러 발생
        return memberRepository.findMemberByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public Member findMemberById(long id) {
        return memberRepository.findMemberById(id).orElseThrow(MemberNotFoundException::new);
    }

    @Override
    // 회원 정보가 유효한지 확인
    public boolean isValidMember(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        Member member = findMemberByEmail(memberDto.getEmail());
        // DTO로 전달된 비밀번호와 DB에 저장된 암호화된 비밀번호가 같은지 확인
        if (passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidPassword(Member member, PasswordRequest passwordRequest, PasswordEncoder passwordEncoder) {

        if(passwordEncoder.matches(passwordRequest.getOldPassword(), member.getPassword())) {
            return true;
        } else {
            throw new PasswordNotMatchedException();
        }
    }

    @Override
    @Transactional
    public void updateMemberProfile(Member member, ProfileRequest profileRequest) {
        member.updateProfile(profileRequest.getNickname());
    }

    @Override
    @Transactional
    public void updateMemberPassword(Member member, PasswordRequest passwordRequest, PasswordEncoder passwordEncoder) {
        member.updatePassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
    }

    @Override
    @Transactional
    public void setMemberLocationAddress(Member member, LocationAddressRequest locationAddressRequest) {
        member.setMemberLocationAddress(locationAddressRequest);
    }
}
