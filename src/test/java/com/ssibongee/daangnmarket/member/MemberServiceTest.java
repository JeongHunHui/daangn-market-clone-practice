package com.ssibongee.daangnmarket.member;

import com.ssibongee.daangnmarket.member.exception.MemberNotFoundException;
import com.ssibongee.daangnmarket.member.dto.LocationAddressRequest;
import com.ssibongee.daangnmarket.member.dto.MemberDto;
import com.ssibongee.daangnmarket.member.dto.PasswordRequest;
import com.ssibongee.daangnmarket.member.dto.ProfileRequest;
import com.ssibongee.daangnmarket.member.domain.entity.Member;
import com.ssibongee.daangnmarket.member.domain.repository.MemberRepository;
import com.ssibongee.daangnmarket.member.exception.PasswordNotMatchedException;
import com.ssibongee.daangnmarket.member.service.GeneralMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// 테스트 메소드에 특정 클래스를 확장하는 어노테이션
// -> @ExtendWith(MokitoExtention.class): Mokito의 Mock객체를 사용하게 해줌
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    // memberService가 만들어질 때 해당 테스트 코드의 @Mock으로 등록된 객체를 주입받음
    @InjectMocks
    private GeneralMemberService memberService;

    // 스프링 컨테이너가 아닌 Mockito 환경에 가짜로 객체를 띄움
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private MemberDto memberDto;

    private Member member;

    private PasswordRequest passwordRequest;

    private ProfileRequest profileRequest;

    private LocationAddressRequest locationAddressRequest;

    // 각 테스트 메소드가 실행되기 전에 실행되는 메소드를 표시
    // 보통 테스트에 사용될 데이터를 세팅하기 위해 사용
    @BeforeEach
    void setUp() {
        // 가짜 passwordEncoder를 선언했으므로, 안에 메소드의 동작을 따로 정의해줘야함
        // -> encode를 하면 "1q2w3e4r!"를 반환하도록 설정
        when(passwordEncoder.encode(any())).thenReturn("1q2w3e4r!");
        memberDto = MemberDto.builder()
                .email("daangnmarket@admin.com")
                .password("1q2w3e4r!")
                .nickname("김당근")
                .build();

        passwordRequest = new PasswordRequest("1q2w3e4r!", "1q2w3e4r5t!");

        profileRequest = new ProfileRequest("daangnmarket@admin.com", "김오이");

        locationAddressRequest = LocationAddressRequest.builder()
                .state("서울특별시")
                .city("관악구")
                .town("은천동")
                .longitude(126.94250287828)
                .latitude(37.4853777674734)
                .build();


        member = MemberDto.toEntity(memberDto, passwordEncoder);
    }

    // 테스트 메소드를 표시
    @Test
    // 테스트 클래스나 테스트 메소드에 이름을 붙혀줄 때 사용
    // DisplayName에는 테스트 상황과 예상 결과까지 적어놓자
    @DisplayName("중복된 이메일이 존재하지 않는 경우 FALSE를 반환한다.")
    void isNotDuplicatedEmailExist() {
        // given
        // memberRepository의 existsByEmail메소드를 실행했을 때 나올 값을 임의로 정해둔다
        // when(메소드).thenReturn(메소드의 반환값)
        when(memberRepository.existsByEmail(any())).thenReturn(false);

        // then
        // 이메일 중복여부가 false여야함
        assertFalse(memberService.isDuplicatedEmail(member.getEmail()));
    }

    @Test
    @DisplayName("중복된 이메일이 존재하는 경우 TRUE를 반환한다.")
    void isDuplicatedEmailExist() {
        // given
        when(memberRepository.existsByEmail(any())).thenReturn(true);

        // then
        assertTrue(memberService.isDuplicatedEmail(member.getEmail()));
    }

    @Test
    @DisplayName("해당 이메일로 가입된 회원이 존재하는 경우 정상적으로 사용자를 조회한다.")
    void isExistMemberFindByEmail() {
        // given
        when(memberRepository.findMemberByEmail(any())).thenReturn(Optional.of(member));

        // when
        Member findByEmailMember = memberService.findMemberByEmail(member.getEmail());

        // then
        assertThat(findByEmailMember).isNotNull();
        assertThat(findByEmailMember.getId()).isEqualTo(member.getId());
        assertThat(findByEmailMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("해당 이메일로 가입된 회원이 존재하지 않는 경우 MemberNotFoundException 예외를 발생시킨다.")
    void isNotExistMemberFindByEmail() {
        // given
        when(memberRepository.findMemberByEmail(any())).thenReturn(Optional.empty());

        // then
        // 2번째 인자의 코드의 결괏값으로 MemberNotFoundException가 나와야 통과
        assertThrows(MemberNotFoundException.class, () -> {
            Member findByEmailMember = memberService.findMemberByEmail(member.getEmail());
        });
    }

    @Test
    @DisplayName("사용자가 로그인 요청시 패스워드가 일치하면 TRUE를 반환한다.")
    void isCorrectPassword() {
        // given
        when(memberRepository.findMemberByEmail(any())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // then
        assertTrue(memberService.isValidMember(memberDto, passwordEncoder));
    }

    @Test
    @DisplayName("사용자가 로그인 요청시 패스워드가 일치하지 않으면 FALSE를 반환한다.")
    void isNotCorrectPassword() {
        // given
        when(memberRepository.findMemberByEmail(any())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // then
        assertFalse(memberService.isValidMember(memberDto, passwordEncoder));
    }

    @Test
    @DisplayName("사용자가 패스워드 변경을 위해 이전 패스워드를 정확히 입력한 경우 TRUE를 반환한다.")
    void isValidOldPassword() {
        // given
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // then
        assertTrue(memberService.isValidPassword(member, passwordRequest, passwordEncoder));
    }

    @Test
    @DisplayName("사용자가 패스워드 변경을 위해 이전 패스워드를 틀리게 입력한 경우 PasswordNotMatchedException을 발생시킨다.")
    void isNotValidOldPassword() {
        // given
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // then
        assertThrows(PasswordNotMatchedException.class, () -> {
            memberService.isValidPassword(member, passwordRequest, passwordEncoder);
        });
    }

    @Test
    @DisplayName("사용자의 프로필이 정상적으로 변경되는 경우 사용자의 프로필 정보와 변경을 요청한 정보가 일치한다.")
    void successToUpdateMemberProfile() {
        // when
        memberService.updateMemberProfile(member, profileRequest);

        // then
        assertEquals(member.getNickname(), profileRequest.getNickname());
    }

    @Test
    @DisplayName("사용자 패스워드 변경에 성공한 경우 사용자의 패스워드가 변경된 패스워드와 일치한다.")
    void successToUpdatePassword() {
        // given
        when(passwordEncoder.encode(any())).thenReturn(passwordRequest.getNewPassword());

        // when
        memberService.updateMemberPassword(member, passwordRequest, passwordEncoder);

        // then
        assertEquals(member.getPassword(), passwordRequest.getNewPassword());
    }

    @Test
    @DisplayName("사용자 위치정보 등록에 성공한 경우 사용자의 위치정보가 변경을 요청한 위치정보와 일치한다.")
    void successToUpdateMemberLocationAndAddress() {
        // when
        memberService.setMemberLocationAddress(member, locationAddressRequest);

        // then
        assertThat(member.getAddress()).isNotNull();
        assertThat(member.getLocation()).isNotNull();
        assertEquals(member.getAddress().getState(), locationAddressRequest.getState());
        assertEquals(member.getAddress().getCity(), locationAddressRequest.getCity());
        assertEquals(member.getAddress().getTown(), locationAddressRequest.getTown());
        assertEquals(member.getLocation().getLongitude(), locationAddressRequest.getLongitude());
        assertEquals(member.getLocation().getLatitude(), locationAddressRequest.getLatitude());
    }

}