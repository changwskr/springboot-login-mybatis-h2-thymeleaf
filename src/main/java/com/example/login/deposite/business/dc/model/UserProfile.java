package com.example.login.deposite.business.dc.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 사용자 프로필 모델 클래스
 * 
 * 사용자 프로필 정보를 담는 모델 클래스입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private String id;      // 사용자 ID
    private String name;    // 이름
    private String phone;   // 전화번호
    private String address; // 주소
} 