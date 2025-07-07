package com.example.login.deposite.business.dc.dao;

import com.example.login.deposite.business.dc.model.CommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 공통 정보 Repository
 * 
 * CommonEntity에 대한 데이터베이스 접근을 제공합니다.
 */
@Repository
public interface CommonRepository extends JpaRepository<CommonEntity, Long> {

    /**
     * 터미널 ID로 조회
     */
    List<CommonEntity> findByTerminalID(String terminalID);

    /**
     * 사용자 ID로 조회
     */
    List<CommonEntity> findByUserID(String userID);

    /**
     * 거래 번호로 조회
     */
    Optional<CommonEntity> findByTransactionNo(String transactionNo);

    /**
     * 요청명으로 조회
     */
    List<CommonEntity> findByReqName(String reqName);

    /**
     * 은행 코드와 지점 코드로 조회
     */
    List<CommonEntity> findByBankCodeAndBranchCode(String bankCode, String branchCode);

    /**
     * 시스템 날짜 범위로 조회
     */
    @Query("SELECT c FROM CommonEntity c WHERE c.systemDate BETWEEN :startDate AND :endDate")
    List<CommonEntity> findBySystemDateBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 업무 날짜로 조회
     */
    List<CommonEntity> findByBusinessDate(String businessDate);

    /**
     * 채널 타입으로 조회
     */
    List<CommonEntity> findByChannelType(String channelType);

    /**
     * 사용자 레벨로 조회
     */
    List<CommonEntity> findByUserLevel(Integer userLevel);

    /**
     * IP 주소로 조회
     */
    List<CommonEntity> findByIpAddress(String ipAddress);

    /**
     * 거래 코드로 조회
     */
    List<CommonEntity> findByTxcode(String txcode);

    /**
     * 생성일시 범위로 조회
     */
    List<CommonEntity> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 특정 사용자의 최근 거래 조회
     */
    @Query("SELECT c FROM CommonEntity c WHERE c.userID = :userID ORDER BY c.createdDate DESC")
    List<CommonEntity> findRecentTransactionsByUserID(@Param("userID") String userID);

    /**
     * 터미널별 거래 수 조회
     */
    @Query("SELECT c.terminalID, COUNT(c) FROM CommonEntity c GROUP BY c.terminalID")
    List<Object[]> countTransactionsByTerminal();

    /**
     * 요청명별 통계 조회
     */
    @Query("SELECT c.reqName, COUNT(c) FROM CommonEntity c GROUP BY c.reqName")
    List<Object[]> countByReqName();
} 