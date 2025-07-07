package com.example.login.deposite.web.controller;

import com.example.login.deposite.business.dc.CommonDC;
import com.example.login.deposite.business.dc.model.CommonEntity;
import com.example.login.deposite.transfer.CommonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Common 정보 컨트롤러
 * 
 * COMMON_INFO 테이블에 대한 웹 접근을 제공합니다.
 */
@Controller
@RequestMapping("/common")
public class CommonController {

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private CommonDC commonDC;

    /**
     * Common 정보 목록 페이지
     */
    @GetMapping("/list")
    public String list(Model model) {
        log.info("[GET] /common/list - Common 정보 목록 조회");
        
        List<CommonEntity> commonList = commonDC.findAll();
        model.addAttribute("commonList", commonList);
        model.addAttribute("totalCount", commonList.size());
        
        log.info("[GET] /common/list - {} 건의 Common 정보 조회됨", commonList.size());
        return "common/list";
    }

    /**
     * 사용자별 거래 이력 페이지
     */
    @GetMapping("/user/{userID}")
    public String userHistory(@PathVariable String userID, Model model) {
        log.info("[GET] /common/user/{} - 사용자별 거래 이력 조회", userID);
        
        List<CommonEntity> userTransactions = commonDC.findRecentTransactionsByUserID(userID);
        model.addAttribute("userID", userID);
        model.addAttribute("transactions", userTransactions);
        model.addAttribute("transactionCount", userTransactions.size());
        
        return "common/user-history";
    }

    /**
     * 터미널별 거래 통계 페이지
     */
    @GetMapping("/stats/terminal")
    public String terminalStats(Model model) {
        log.info("[GET] /common/stats/terminal - 터미널별 거래 통계 조회");
        
        List<Object[]> terminalStats = commonDC.getTransactionCountByTerminal();
        model.addAttribute("terminalStats", terminalStats);
        
        return "common/terminal-stats";
    }

    /**
     * 요청명별 통계 페이지
     */
    @GetMapping("/stats/request")
    public String requestStats(Model model) {
        log.info("[GET] /common/stats/request - 요청명별 통계 조회");
        
        List<Object[]> requestStats = commonDC.getCountByReqName();
        model.addAttribute("requestStats", requestStats);
        
        return "common/request-stats";
    }

    /**
     * Common 정보 상세 조회
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.info("[GET] /common/detail/{} - Common 정보 상세 조회", id);
        
        Optional<CommonEntity> commonEntity = commonDC.findById(id);
        if (commonEntity.isPresent()) {
            model.addAttribute("commonInfo", commonEntity.get());
            log.info("[GET] /common/detail/{} - Common 정보 조회 완료", id);
        } else {
            model.addAttribute("error", "해당 ID의 Common 정보를 찾을 수 없습니다.");
            log.warn("[GET] /common/detail/{} - Common 정보 없음", id);
        }
        
        return "common/detail";
    }
}

/**
 * Common 정보 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/common")
class CommonRestController {

    private static final Logger log = LoggerFactory.getLogger(CommonRestController.class);

    @Autowired
    private CommonDC commonDC;

    /**
     * 모든 Common 정보 조회 (REST API)
     */
    @GetMapping("/all")
    public ResponseEntity<List<CommonEntity>> getAllCommon() {
        log.info("[API] GET /api/common/all - 모든 Common 정보 조회");
        
        List<CommonEntity> commonList = commonDC.findAll();
        
        log.info("[API] {} 건의 Common 정보 반환", commonList.size());
        return ResponseEntity.ok(commonList);
    }

    /**
     * 사용자별 거래 이력 조회 (REST API)
     */
    @GetMapping("/user/{userID}")
    public ResponseEntity<List<CommonEntity>> getUserTransactions(@PathVariable String userID) {
        log.info("[API] GET /api/common/user/{} - 사용자별 거래 이력 조회", userID);
        
        List<CommonEntity> userTransactions = commonDC.findRecentTransactionsByUserID(userID);
        
        log.info("[API] 사용자 {} - {} 건의 거래 이력 반환", userID, userTransactions.size());
        return ResponseEntity.ok(userTransactions);
    }

    /**
     * 터미널별 거래 통계 조회 (REST API)
     */
    @GetMapping("/stats/terminal")
    public ResponseEntity<List<Object[]>> getTerminalStats() {
        log.info("[API] GET /api/common/stats/terminal - 터미널별 거래 통계 조회");
        
        List<Object[]> terminalStats = commonDC.getTransactionCountByTerminal();
        
        log.info("[API] {} 개 터미널의 통계 반환", terminalStats.size());
        return ResponseEntity.ok(terminalStats);
    }

    /**
     * 요청명별 통계 조회 (REST API)
     */
    @GetMapping("/stats/request")
    public ResponseEntity<List<Object[]>> getRequestStats() {
        log.info("[API] GET /api/common/stats/request - 요청명별 통계 조회");
        
        List<Object[]> requestStats = commonDC.getCountByReqName();
        
        log.info("[API] {} 개 요청명의 통계 반환", requestStats.size());
        return ResponseEntity.ok(requestStats);
    }

    /**
     * 거래번호로 조회 (REST API)
     */
    @GetMapping("/transaction/{transactionNo}")
    public ResponseEntity<CommonEntity> getByTransactionNo(@PathVariable String transactionNo) {
        log.info("[API] GET /api/common/transaction/{} - 거래번호로 조회", transactionNo);
        
        Optional<CommonEntity> commonEntity = commonDC.findByTransactionNo(transactionNo);
        
        if (commonEntity.isPresent()) {
            log.info("[API] 거래번호 {} 정보 찾음", transactionNo);
            return ResponseEntity.ok(commonEntity.get());
        } else {
            log.warn("[API] 거래번호 {} 정보 없음", transactionNo);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 터미널 ID로 조회 (REST API)
     */
    @GetMapping("/terminal/{terminalID}")
    public ResponseEntity<List<CommonEntity>> getByTerminalID(@PathVariable String terminalID) {
        log.info("[API] GET /api/common/terminal/{} - 터미널 ID로 조회", terminalID);
        
        List<CommonEntity> terminalTransactions = commonDC.findByTerminalID(terminalID);
        
        log.info("[API] 터미널 {} - {} 건의 거래 반환", terminalID, terminalTransactions.size());
        return ResponseEntity.ok(terminalTransactions);
    }

    /**
     * 요청명으로 조회 (REST API)
     */
    @GetMapping("/request/{reqName}")
    public ResponseEntity<List<CommonEntity>> getByReqName(@PathVariable String reqName) {
        log.info("[API] GET /api/common/request/{} - 요청명으로 조회", reqName);
        
        List<CommonEntity> requestTransactions = commonDC.findByReqName(reqName);
        
        log.info("[API] 요청명 {} - {} 건의 거래 반환", reqName, requestTransactions.size());
        return ResponseEntity.ok(requestTransactions);
    }
} 