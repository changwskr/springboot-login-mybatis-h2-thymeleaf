 package com.example.login.deposite.business.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 예금 상세 데이터 전송 객체 (비즈니스)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositeDDTO {
    private String id;       // ID
    private String name;     // 이름
    private String company;  // 회사명
    private String contact;  // 연락처
    private String address;  // 주소
}
