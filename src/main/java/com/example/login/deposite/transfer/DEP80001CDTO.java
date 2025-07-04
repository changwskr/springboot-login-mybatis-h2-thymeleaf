package com.example.login.deposite.transfer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import com.example.login.deposite.business.dto.DepositeDDTO;

/**
 * DEP80001 공통 데이터 전송 객체
 * 
 * 예금 관련 API 요청/응답을 위한 공통 DTO 클래스입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DEP80001CDTO {
    
    // 거래 관련 필드
    private String txcode;           // 거래 코드
    private String actionName;       // HTTP 메서드 (put, post, delete, get)
    private String routing_page;     // 라우팅 페이지
    private String in;               // 입력
    private String out;              // 출력

    ////////////////////////
    // TEST DATA
    ///////////////////////
    private String id;               // 사용자 ID
    private String name;             // 사용자 이름
    private String company;          // 회사명
    private String address;          // 주소
    private String contact;          // 연락처

    private List<DepositeDDTO> ddto; // 예금 상세 DTO 리스트
} 