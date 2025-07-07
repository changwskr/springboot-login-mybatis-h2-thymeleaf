package com.example.login.deposite.transfer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import com.example.login.deposite.business.dto.DepositeDDTO;

/**
 * 예금 공통 데이터 전송 객체
 * 
 * 예금 관련 공통 정보와 상세 정보를 담는 DTO 클래스입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositeCDTO {
    
    // 거래 관련 필드
    private String txcode;           // 거래 코드
    private String routing_page;     // 라우팅 페이지
    private String in;               // 입력
    private String out;              // 출력
    
    // 사용자 정보
    private String id;               // 사용자 ID
    private String name;             // 사용자 이름
    
    // 회사 정보
    private String company;          // 회사명
    private String address;          // 주소
    private String contact;          // 연락처
    
    // 공통 DTO 및 상세 DTO 리스트
    private CommonDTO comDTO;        // 공통 DTO
    private List<DepositeDDTO> dDtoList; // 예금 상세 DTO 리스트
} 