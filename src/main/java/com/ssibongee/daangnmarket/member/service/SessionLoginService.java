package com.ssibongee.daangnmarket.member.service;

import com.ssibongee.daangnmarket.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    // HttpSession을 final로 선언한 이유 알아보기(이것도 DI받는 건가?)
    private final HttpSession httpSession;
    private final MemberService memberService;
    public static final String MEMBER_ID = "MEMBER_ID";

    @Override
    // 세션에 대한 행위를 캡슐화
    public void login(long id) {
        httpSession.setAttribute(MEMBER_ID, id);
    }

    @Override
    public void logout() {
        httpSession.removeAttribute(MEMBER_ID);
    }

    @Override
    public Member getLoginMember() {
        Long memberId = (Long) httpSession.getAttribute(MEMBER_ID);

        return memberService.findMemberById(memberId);
    }

    @Override
    public Long getLoginMemberId() {
        return (Long) httpSession.getAttribute(MEMBER_ID);
    }
}
