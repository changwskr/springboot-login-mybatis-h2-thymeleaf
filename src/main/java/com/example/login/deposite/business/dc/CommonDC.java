package com.example.login.deposite.business.dc;

import com.example.login.deposite.business.dc.dao.CommonRepository;
import com.example.login.deposite.business.dc.model.CommonEntity;
import com.example.login.deposite.transfer.CommonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Common 정보 Domain Controller
 * 
 * CommonEntity와 CommonDTO 간의 변환 및 비즈니스 로직을 처리합니다.
 */
@Service
@Transactional(transactionManager = "jpaTransactionManager")
public class CommonDC {

    private static final Logger log = LoggerFactory.getLogger(CommonDC.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Autowired
    private CommonRepository commonRepository;

    /**
     * CommonDTO를 저장
     */
    public CommonEntity saveCommonDTO(CommonDTO dto) {
        log.info("[SAVE] CommonDTO 저장 시작: {}", dto.getReqName());
        
        CommonEntity entity = convertToEntity(dto);
        CommonEntity savedEntity = commonRepository.save(entity);
        
        log.info("[SAVE] CommonDTO 저장 완료: ID={}", savedEntity.getId());
        return savedEntity;
    }

    /**
     * ID로 조회
     */
    @Transactional(readOnly = true)
    public Optional<CommonEntity> findById(Long id) {
        log.info("[FIND] ID로 조회: {}", id);
        return commonRepository.findById(id);
    }

    /**
     * 거래번호로 조회
     */
    @Transactional(readOnly = true)
    public Optional<CommonEntity> findByTransactionNo(String transactionNo) {
        log.info("[FIND] 거래번호로 조회: {}", transactionNo);
        return commonRepository.findByTransactionNo(transactionNo);
    }

    /**
     * 사용자 ID로 조회
     */
    @Transactional(readOnly = true)
    public List<CommonEntity> findByUserID(String userID) {
        log.info("[FIND] 사용자 ID로 조회: {}", userID);
        return commonRepository.findByUserID(userID);
    }

    /**
     * 요청명으로 조회
     */
    @Transactional(readOnly = true)
    public List<CommonEntity> findByReqName(String reqName) {
        log.info("[FIND] 요청명으로 조회: {}", reqName);
        return commonRepository.findByReqName(reqName);
    }

    /**
     * 터미널 ID로 조회
     */
    @Transactional(readOnly = true)
    public List<CommonEntity> findByTerminalID(String terminalID) {
        log.info("[FIND] 터미널 ID로 조회: {}", terminalID);
        return commonRepository.findByTerminalID(terminalID);
    }

    /**
     * 모든 Common 정보 조회
     */
    @Transactional(readOnly = true)
    public List<CommonEntity> findAll() {
        log.info("[FIND] 모든 Common 정보 조회");
        return commonRepository.findAll();
    }

    /**
     * 사용자별 최근 거래 조회
     */
    @Transactional(readOnly = true)
    public List<CommonEntity> findRecentTransactionsByUserID(String userID) {
        log.info("[FIND] 사용자별 최근 거래 조회: {}", userID);
        return commonRepository.findRecentTransactionsByUserID(userID);
    }

    /**
     * 터미널별 거래 수 통계
     */
    @Transactional(readOnly = true)
    public List<Object[]> getTransactionCountByTerminal() {
        log.info("[STAT] 터미널별 거래 수 통계 조회");
        return commonRepository.countTransactionsByTerminal();
    }

    /**
     * 요청명별 통계
     */
    @Transactional(readOnly = true)
    public List<Object[]> getCountByReqName() {
        log.info("[STAT] 요청명별 통계 조회");
        return commonRepository.countByReqName();
    }

    /**
     * CommonEntity를 CommonDTO로 변환
     */
    public CommonDTO convertToDTO(CommonEntity entity) {
        if (entity == null) {
            return null;
        }

        CommonDTO dto = new CommonDTO();
        dto.setTerminalID(entity.getTerminalID());
        dto.setTerminalType(entity.getTerminalType());
        dto.setXmlSeq(entity.getXmlSeq());
        dto.setBankCode(entity.getBankCode());
        dto.setBranchCode(entity.getBranchCode());
        dto.setGlPostBranchCode(entity.getGlPostBranchCode());
        dto.setChannelType(entity.getChannelType());
        dto.setUserID(entity.getUserID());
        dto.setEventNo(entity.getEventNo());
        dto.setNation(entity.getNation());
        dto.setRegionCode(entity.getRegionCode());
        dto.setTimeZone(entity.getTimeZone());
        dto.setFxRateCount(entity.getFxRateCount() != null ? entity.getFxRateCount() : 0);
        dto.setReqName(entity.getReqName());
        dto.setSystemDate(entity.getSystemDate());
        dto.setBusinessDate(entity.getBusinessDate());
        dto.setSystemInTime(entity.getSystemInTime());
        dto.setSystemOutTime(entity.getSystemOutTime());
        dto.setTransactionNo(entity.getTransactionNo());
        dto.setBaseCurrency(entity.getBaseCurrency());
        dto.setMultiPL(entity.getMultiPL());
        dto.setUserLevel(entity.getUserLevel() != null ? entity.getUserLevel() : 0);
        dto.setIPAddress(entity.getIpAddress());
        dto.setTxcode(entity.getTxcode());

        return dto;
    }

    /**
     * CommonDTO를 CommonEntity로 변환
     */
    public CommonEntity convertToEntity(CommonDTO dto) {
        if (dto == null) {
            return null;
        }

        CommonEntity entity = new CommonEntity();
        entity.setTerminalID(dto.getTerminalID());
        entity.setTerminalType(dto.getTerminalType());
        entity.setXmlSeq(dto.getXmlSeq());
        entity.setBankCode(dto.getBankCode());
        entity.setBranchCode(dto.getBranchCode());
        entity.setGlPostBranchCode(dto.getGlPostBranchCode());
        entity.setChannelType(dto.getChannelType());
        entity.setUserID(dto.getUserID());
        entity.setEventNo(dto.getEventNo());
        entity.setNation(dto.getNation());
        entity.setRegionCode(dto.getRegionCode());
        entity.setTimeZone(dto.getTimeZone());
        entity.setFxRateCount(dto.getFxRateCount());
        entity.setReqName(dto.getReqName());
        entity.setSystemDate(dto.getSystemDate());
        entity.setBusinessDate(dto.getBusinessDate());
        entity.setSystemInTime(dto.getSystemInTime());
        entity.setSystemOutTime(dto.getSystemOutTime());
        entity.setTransactionNo(dto.getTransactionNo());
        entity.setBaseCurrency(dto.getBaseCurrency());
        entity.setMultiPL(dto.getMultiPL());
        entity.setUserLevel(dto.getUserLevel());
        entity.setIpAddress(dto.getIPAddress());
        entity.setTxcode(dto.getTxcode());

        // 거래번호가 없으면 자동 생성
        if (entity.getTransactionNo() == null || entity.getTransactionNo().isEmpty()) {
            entity.setTransactionNo(generateTransactionNo());
        }

        // 시스템 날짜/시간이 없으면 현재 시간으로 설정
        if (entity.getSystemDate() == null || entity.getSystemDate().isEmpty()) {
            entity.setSystemDate(LocalDateTime.now().format(DATE_FORMATTER));
        }
        if (entity.getBusinessDate() == null || entity.getBusinessDate().isEmpty()) {
            entity.setBusinessDate(LocalDateTime.now().format(DATE_FORMATTER));
        }
        if (entity.getSystemInTime() == null || entity.getSystemInTime().isEmpty()) {
            entity.setSystemInTime(LocalDateTime.now().format(TIME_FORMATTER));
        }

        return entity;
    }

    /**
     * 거래번호 자동 생성
     */
    private String generateTransactionNo() {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        return "TXN" + timestamp + String.format("%03d", (int)(Math.random() * 1000));
    }
} 