package com.ssibongee.daangnmarket.member.domain.repository;

import com.ssibongee.daangnmarket.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository<@Entity, @ID>를 상속받아서 스프링 데이터 JPA 적용
// 인터페이스만 작성하면 스프링 데이터 JPA가 구현 객체를 동적으로 생성해서 주입
// deleteAll, deleteById, findById, save 등 공통 메소드 제공
public interface MemberRepository extends JpaRepository<Member, Long> {

    public boolean existsByEmail(String email);

    public Optional<Member> findMemberByEmail(String email);

    public Optional<Member> findMemberById(long id);
}
