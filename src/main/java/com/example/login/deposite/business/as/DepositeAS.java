package com.example.login.deposite.business.as;

import com.example.login.deposite.business.dc.model.DMember;
import com.example.login.deposite.business.dto.DepositeDDTO;
import com.example.login.deposite.transfer.DepositeCDTO;
import com.example.login.deposite.business.dc.DepositeDC;
import com.example.login.deposite.business.dc.CommonDC;
import com.example.login.deposite.business.dc.model.CommonEntity;
import com.example.login.deposite.transfer.CommonDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Service
@Transactional(transactionManager = "jpaTransactionManager")
@RequiredArgsConstructor
public class DepositeAS {

    private DepositeDDTO depositeDDTO;
    private DepositeCDTO depositeCDTO;
    private static final Logger log = LoggerFactory.getLogger(DepositeAS.class);

    private final DepositeDC depositeDC;
    private final CommonDC commonDC;
    private String actionName = "";

    // ==================== 메모리 세션 저장소 ====================
    /**
     * 메모리 기반 세션 저장소 (static HashMap)
     * Key: 세션ID (String), Value: CommonDTO
     */
    private static final Map<String, CommonDTO> SESSION_STORAGE = new HashMap<>();
    
    /**
     * 세션별 거래 이력 저장소
     * Key: 세션ID (String), Value: 거래 이력 리스트
     */
    private static final Map<String, List<CommonDTO>> SESSION_HISTORY = new HashMap<>();

    /**
     * 세션 만료 시간 관리 (밀리초 단위)
     * Key: 세션ID (String), Value: 만료 시간 (Long)
     */
    private static final Map<String, Long> SESSION_EXPIRY = new HashMap<>();
    
    /**
     * 세션 타임아웃 (30분)
     */
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000L; // 30분



    public DepositeCDTO execute(DepositeCDTO depositeCDTO) {
        log.info("[START] DepositeAS.execute - 요청 처리 시작");

        /*
        * 1) CommonDTO 설정
         */
        CommonDTO commonDTO = depositeCDTO.getComDTO();

        /*
        2) ACTIONNAME 설정
         */
        this.actionName = commonDTO.getReqName();

        String sessionId = commonDTO.getTransactionNo(); // 거래번호를 세션ID로 사용
        log.info("요청된 actionName: {}, sessionId: {}", actionName, sessionId);

        // CommonDTO를 메모리 세션에 저장
        if (sessionId != null && !sessionId.isEmpty()) {
            putSession(sessionId, commonDTO);
            log.info("세션 저장 완료: sessionId={}", sessionId);
        }

        // CommonDTO를 DB에 저장 (거래 이력 관리)
        try {
            CommonEntity savedCommon = commonDC.saveCommonDTO(commonDTO);
            log.info("Common 정보 DB 저장 완료: ID={}, TransactionNo={}", 
                    savedCommon.getId(), savedCommon.getTransactionNo());
        } catch (Exception e) {
            log.error("Common 정보 DB 저장 중 오류 발생: {}", e.getMessage(), e);
        }

        this.depositeCDTO = depositeCDTO;
        this.depositeDDTO = new DepositeDDTO();
        this.depositeDDTO.setCompany(this.depositeCDTO.getCompany());
        this.depositeDDTO.setId(this.depositeCDTO.getId());
        this.depositeDDTO.setName(depositeCDTO.getName());

        List<DepositeDDTO> allList = new ArrayList<>();

        switch (actionName == null ? "" : actionName.toLowerCase()) {
            case "put":
                log.info("PUT 요청 처리 시작");
                depositeDDTO = depositeDC.restPUT(this.depositeDDTO);
                allList.add(depositeDDTO);
                break;
            case "post":
                log.info("POST 요청 처리 시작");
                depositeDDTO = depositeDC.restPOST(this.depositeDDTO);
                allList.add(depositeDDTO);
                break;
            case "delete":
                log.info("DELETE 요청 처리 시작");
                int rtn = depositeDC.restDELETE(this.depositeDDTO);
                allList.add(depositeDDTO); // 삭제 후에도 반환 정보 저장
                log.info("삭제 처리 결과: {}", rtn);
                break;
            case "get":
                log.info("GET 요청 처리 시작");
                depositeDDTO = depositeDC.restGET(this.depositeDDTO);
                allList.add(depositeDDTO);
                break;
            case "getall":
                log.info("GETALL 요청 처리 시작");
                List<DepositeDDTO> all = depositeDC.restGETALL();
                if (!all.isEmpty()) {
                    depositeDDTO = all.get(0);
                }
                allList = all;
                break;
            default:
                log.warn("알 수 없는 actionName 처리 요청: {}", actionName);
                break;
        }

        for (DepositeDDTO dto : allList) {
            log.info("처리 결과: id={}, name={}", dto.getId(), dto.getName());
        }

        depositeCDTO.setDdto(allList);

        log.info("[END] DepositeAS.execute - 요청 처리 완료");
        return this.depositeCDTO;
    }

    /**
     * 사용자별 최근 거래 이력 조회
     */
    public List<CommonEntity> getUserTransactionHistory(String userID) {
        log.info("[HISTORY] 사용자별 거래 이력 조회: {}", userID);
        return commonDC.findRecentTransactionsByUserID(userID);
    }

    /**
     * 요청명별 통계 조회
     */
    public List<Object[]> getRequestStatistics() {
        log.info("[STAT] 요청명별 통계 조회");
        return commonDC.getCountByReqName();
    }

    /**
     * 터미널별 거래 수 통계 조회
     */
    public List<Object[]> getTerminalStatistics() {
        log.info("[STAT] 터미널별 거래 수 통계 조회");
        return commonDC.getTransactionCountByTerminal();
    }

    // ==================== 세션 관리 메서드 ====================
    
    /**
     * 세션에 CommonDTO 저장
     * @param sessionId 세션 ID
     * @param commonDTO 저장할 CommonDTO
     */
    public void putSession(String sessionId, CommonDTO commonDTO) {
        log.info("[SESSION] 세션 저장: sessionId={}, reqName={}", sessionId, commonDTO.getReqName());
        
        // 만료된 세션 정리
        cleanExpiredSessions();
        
        // 세션 저장
        SESSION_STORAGE.put(sessionId, commonDTO);
        
        // 만료 시간 설정
        SESSION_EXPIRY.put(sessionId, System.currentTimeMillis() + SESSION_TIMEOUT);
        
        // 거래 이력에 추가
        addToSessionHistory(sessionId, commonDTO);
        
        log.info("[SESSION] 세션 저장 완료: sessionId={}, 총 세션 수={}", sessionId, SESSION_STORAGE.size());
    }
    
    /**
     * 세션에서 CommonDTO 조회
     * @param sessionId 세션 ID
     * @return CommonDTO 또는 null
     */
    public CommonDTO getSession(String sessionId) {
        log.info("[SESSION] 세션 조회: sessionId={}", sessionId);
        
        // 만료된 세션 정리
        cleanExpiredSessions();
        
        // 세션 존재 여부 확인
        if (!SESSION_STORAGE.containsKey(sessionId)) {
            log.warn("[SESSION] 세션이 존재하지 않음: sessionId={}", sessionId);
            return null;
        }
        
        // 만료 시간 확인
        Long expiryTime = SESSION_EXPIRY.get(sessionId);
        if (expiryTime == null || System.currentTimeMillis() > expiryTime) {
            log.warn("[SESSION] 세션이 만료됨: sessionId={}", sessionId);
            removeSession(sessionId);
            return null;
        }
        
        CommonDTO commonDTO = SESSION_STORAGE.get(sessionId);
        log.info("[SESSION] 세션 조회 완료: sessionId={}, reqName={}", sessionId, 
                commonDTO != null ? commonDTO.getReqName() : "null");
        
        return commonDTO;
    }
    
    /**
     * 세션 삭제
     * @param sessionId 세션 ID
     */
    public void removeSession(String sessionId) {
        log.info("[SESSION] 세션 삭제: sessionId={}", sessionId);
        
        SESSION_STORAGE.remove(sessionId);
        SESSION_EXPIRY.remove(sessionId);
        // 이력은 유지 (필요시 삭제 가능)
        
        log.info("[SESSION] 세션 삭제 완료: sessionId={}, 남은 세션 수={}", sessionId, SESSION_STORAGE.size());
    }
    
    /**
     * 모든 세션 조회
     * @return 모든 세션의 Map
     */
    public Map<String, CommonDTO> getAllSessions() {
        log.info("[SESSION] 모든 세션 조회");
        cleanExpiredSessions();
        return new HashMap<>(SESSION_STORAGE);
    }
    
    /**
     * 세션별 거래 이력 조회
     * @param sessionId 세션 ID
     * @return 거래 이력 리스트
     */
    public List<CommonDTO> getSessionHistory(String sessionId) {
        log.info("[SESSION] 세션별 거래 이력 조회: sessionId={}", sessionId);
        
        List<CommonDTO> history = SESSION_HISTORY.get(sessionId);
        if (history == null) {
            history = new ArrayList<>();
            log.info("[SESSION] 해당 세션의 이력이 없음: sessionId={}", sessionId);
        } else {
            log.info("[SESSION] 세션별 이력 조회 완료: sessionId={}, 이력 수={}", sessionId, history.size());
        }
        
        return new ArrayList<>(history); // 복사본 반환
    }
    
    /**
     * 세션 이력에 추가
     * @param sessionId 세션 ID
     * @param commonDTO 추가할 CommonDTO
     */
    private void addToSessionHistory(String sessionId, CommonDTO commonDTO) {
        SESSION_HISTORY.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(commonDTO);
        log.info("[SESSION] 이력 추가: sessionId={}, 총 이력 수={}", sessionId, 
                SESSION_HISTORY.get(sessionId).size());
    }
    
    /**
     * 만료된 세션 정리
     */
    private void cleanExpiredSessions() {
        long currentTime = System.currentTimeMillis();
        List<String> expiredSessions = new ArrayList<>();
        
        for (Map.Entry<String, Long> entry : SESSION_EXPIRY.entrySet()) {
            if (currentTime > entry.getValue()) {
                expiredSessions.add(entry.getKey());
            }
        }
        
        for (String sessionId : expiredSessions) {
            log.info("[SESSION] 만료된 세션 정리: sessionId={}", sessionId);
            SESSION_STORAGE.remove(sessionId);
            SESSION_EXPIRY.remove(sessionId);
            // 이력은 유지하여 나중에 조회 가능
        }
        
        if (!expiredSessions.isEmpty()) {
            log.info("[SESSION] 만료된 세션 정리 완료: {} 개 세션 삭제", expiredSessions.size());
        }
    }
    
    /**
     * 세션 통계 정보 조회
     * @return 세션 통계 정보
     */
    public Map<String, Object> getSessionStatistics() {
        cleanExpiredSessions();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSessions", SESSION_STORAGE.size());
        stats.put("totalHistoryEntries", SESSION_HISTORY.values().stream()
                .mapToInt(List::size).sum());
        stats.put("sessionTimeout", SESSION_TIMEOUT);
        
        log.info("[SESSION] 세션 통계: {}", stats);
        return stats;
    }
}
