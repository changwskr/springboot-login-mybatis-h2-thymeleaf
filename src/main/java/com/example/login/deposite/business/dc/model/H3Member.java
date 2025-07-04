package com.example.login.deposite.business.dc.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * H3 회원 모델 클래스
 * 
 * H3 데이터베이스의 회원 정보를 담는 모델 클래스입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class H3Member {

    private Long id;        // 시스템이 정하는 아이디
    private String name;    // 회원명
    private String address; // 주소
    private String contact; // 연락처
} 