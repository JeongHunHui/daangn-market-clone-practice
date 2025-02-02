package com.ssibongee.daangnmarket.member.controller;

import com.ssibongee.daangnmarket.commons.annotation.LoginMember;
import com.ssibongee.daangnmarket.commons.annotation.LoginRequired;
import com.ssibongee.daangnmarket.member.domain.entity.Member;
import com.ssibongee.daangnmarket.member.dto.*;
import com.ssibongee.daangnmarket.member.service.LoginService;
import com.ssibongee.daangnmarket.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.ssibongee.daangnmarket.commons.HttpStatusResponseEntity.*;
import static com.ssibongee.daangnmarket.member.controller.MemberController.*;

// Controller 클래스로 Bean에 등록
// @Controller + @ResponseBody 와 동일
@RestController
@RequiredArgsConstructor
// 로깅에 대한 추상 레이어를 제공하는 인터페이스의 모음, log.어쩌구로 로깅 가능
@Slf4j
// 해당 url로 들어오는 http request와 클래스, 메소드를 매핑해줌
@RequestMapping(MEMBER_API_URI)
public class MemberController {

    public static final String MEMBER_API_URI = "/api/members";

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;

    // 해당 URL로 들어오는 Post요청과 메소드 매핑
    @PostMapping
    // ResponseEntity: 사용자의 HttpRequest에 대한 응답 데이터
    //                 (HttpStatus, HttpHeaders, HttpBody)를 포함하는 클래스
    // @RequeestBody: Http 요청의 body를 자바 객체로 변환해서 매핑된 메소드 파라미터로 전달
    // @Valid: 클라이언트의 입력 데이터가 dto 클래스로 넘어올 때 유효성 체크
    // -> dto 클래스의 어노테이션을 기준으로 유효성 체크
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid MemberDto memberDto) {

        // 클라이언트에서 사용자 이메일 중복체크를 수행하지만 API요청에 의한 예외상황에 대비하여 더블체크
        boolean isDuplicated = memberService.isDuplicatedEmail(memberDto.getEmail());

        if (isDuplicated) {
            return RESPONSE_CONFLICT;
        }

        // 요청으로 받은 MemberDto를 Member 엔티티로 변환 후 회원 등록
        Member member = MemberDto.toEntity(memberDto, passwordEncoder);
        memberService.registrationMember(member);

        return RESPONSE_OK;
    }

    @GetMapping("/duplicated/{email}")
    // @PathVariable: URL 경로에 {변수명} 을 경로변수로 받아옴
    public ResponseEntity<HttpStatus> isDuplicatedEmail(@PathVariable String email) {
        boolean isDuplicated = memberService.isDuplicatedEmail(email);

        if (isDuplicated) {
            return RESPONSE_CONFLICT;
        }
        return RESPONSE_OK;
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody @Valid MemberDto memberDto) {

        boolean isValidMember = memberService.isValidMember(memberDto, passwordEncoder);

        if (isValidMember) {
            // 암호화 및 로그인 정보 저장은 controller layer가 아닌 service layer에서 처리
            loginService.login(memberService.findMemberByEmail(memberDto.getEmail()).getId());
            return RESPONSE_OK;
        }

        return RESPONSE_BAD_REQUEST;
    }

    // 일단 HTTPRequest가 들어오면 Dispatcher Servlet은 HandlerMapping에게 핸들러를 찾도록 요청
    // -> 로그인 인터셉터의 preHandler로 이동해서 인터셉터 실행
    // -> @LoginRequired가 붙어있으면 세션에 로그인 정보가 있는지 확인하고 있으면 정상 실행
    // -> 세션에 로그인 정보가 없으면 UnAuthenticatedAccessException 예외 발생
    @LoginRequired
    @GetMapping("/logout")
    public ResponseEntity<HttpStatus> logout() {
        loginService.logout();
        return RESPONSE_OK;
    }

    @LoginRequired
    @GetMapping("/my-profile")
    // 로그인한 사용자의 정보를 가져올때 같은 코드가 반복됨
    // 이 정보를 커스텀 ArgumentResolver를 구현해서 Controller의 인자로 받아올 수 있게 하면 중복코드 줄어들음
    // @LoginMember 어노테이션이 붙어있으면 LoginMemberArgumentResolver가
    // 해당 파라미터에 세션에 저장되어 있는 member의 정보를 바인딩해준다.
    public ResponseEntity<ProfileResponse> getMemberProfile(@LoginMember Member member) {

        return ResponseEntity.ok(ProfileResponse.of(member));
    }

    @LoginRequired
    @PutMapping("/my-profile")
    public ResponseEntity<ProfileResponse> updateMemberProfile(@LoginMember Member member, @RequestBody ProfileRequest profileRequest) {

        memberService.updateMemberProfile(member, profileRequest);

        return ResponseEntity.ok(ProfileResponse.of(member));
    }


    @LoginRequired
    @PutMapping("/password")
    public ResponseEntity<HttpStatus> changePassword(@LoginMember Member member,  @Valid @RequestBody PasswordRequest passwordRequest) {

        if(memberService.isValidPassword(member, passwordRequest, passwordEncoder)) {
            memberService.updateMemberPassword(member, passwordRequest, passwordEncoder);
        }

        return RESPONSE_OK;
    }

    @LoginRequired
    @PutMapping("/my-location")
    public ResponseEntity<HttpStatus> setMemberLocationAddress(@LoginMember Member member, @RequestBody LocationAddressRequest locationAddressRequest) {

        memberService.setMemberLocationAddress(member, locationAddressRequest);

        return RESPONSE_OK;
    }

}
