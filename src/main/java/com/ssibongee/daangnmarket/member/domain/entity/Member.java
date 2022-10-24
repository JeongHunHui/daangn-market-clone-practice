package com.ssibongee.daangnmarket.member.domain.entity;

import com.ssibongee.daangnmarket.member.dto.LocationAddressRequest;
import com.ssibongee.daangnmarket.post.domain.entity.Address;
import com.ssibongee.daangnmarket.post.domain.entity.Location;
import lombok.*;

import javax.persistence.*;

// JPA가 관리하는 클래스, Name 속성으로 엔티티 이름 지정가능(보통 기본값인 클래스 이름 사용)
@Entity
// Lombok, getter 메소드들을 생성
@Getter
// 엔티티와 매핑할 테이블을 name 속성으로 지정(기본값은 엔티티 이름)
@Table(name = "MEMBER")
// Lombok, 기본 생성자 생성, access 속성으로 접근제한자 설정 가능
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    // 기본 키 매핑
    @Id
    // 기본키 생성 전략, GenerationType.IDENTITY = AUTO_INCREMENT
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 객체 필드를 테이블 컬럼에 매핑, name 속성, nullable 속성(기본 true), unique 속성 등
    @Column(name = "MEMBER_ID")
    private Long id;

    private String email;

    private String password;

    private String nickname;

    // 임베디드 타입 속성 표시(Address는 도시, 우편번호 등의 여러 값을 가진 타입)
    // 연관되어 있는 속성끼리 모아서 가독성 증가
    @Embedded
    private Address address;

    @Embedded
    private Location location;

    // Lombok, Builder 패턴을 간단하게 구현
    @Builder
    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void updateProfile(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void setMemberLocationAddress(LocationAddressRequest locationAddress) {
        this.address = locationAddress.toAddress();
        this.location = locationAddress.toLocation();
    }

}
