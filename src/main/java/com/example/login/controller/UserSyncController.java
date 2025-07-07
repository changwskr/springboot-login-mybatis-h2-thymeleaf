package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.UserSyncService;
import com.example.login.deposite.transfer.CommonDTO;
import com.example.login.deposite.business.dc.CommonDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users/sync")
public class UserSyncController {

    private static final Logger logger = LoggerFactory.getLogger(UserSyncController.class);

    @Autowired
    private UserSyncService userSyncService;

    @Autowired
    private CommonDC commonDC;

    private List<String> jobOptions = Arrays.asList("Developer", "Manager", "Designer", "Tester");
    private List<String> companyOptions = Arrays.asList("OpenAI", "Google", "Amazon", "Microsoft");

    /**
     * 두 DB의 모든 사용자 목록 조회
     */
    @GetMapping
    public String listAllUsers(Model model) {
        logger.info("=== UserSyncController.listAllUsers() START ===");
        try {
            List<User> users = userSyncService.findAllFromBothDatabases();
            model.addAttribute("users", users);
            logger.info("=== UserSyncController.listAllUsers() END - Found {} users ===", users.size());
            return "users/sync/list";
        } catch (Exception e) {
            logger.error("=== UserSyncController.listAllUsers() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자 등록 폼 표시
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.info("=== UserSyncController.showCreateForm() START ===");
        try {
            model.addAttribute("user", new User());
            model.addAttribute("jobs", jobOptions);
            model.addAttribute("companies", companyOptions);
            logger.info("=== UserSyncController.showCreateForm() END ===");
            return "users/sync/create";
        } catch (Exception e) {
            logger.error("=== UserSyncController.showCreateForm() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자를 두 DB에 동시에 등록
     */
    @PostMapping
    public String createUserInBothDatabases(@Valid @ModelAttribute User user, BindingResult result, 
                                          @RequestParam(required = false) String terminalId,
                                          @RequestParam(required = false) String terminalType,
                                          @RequestParam(required = false) String bankCode,
                                          @RequestParam(required = false) String branchCode,
                                          @RequestParam(required = false) String channelType,
                                          @RequestParam(required = false) String userId,
                                          @RequestParam(required = false) String nation,
                                          @RequestParam(required = false) String ipAddress,
                                          Model model, HttpServletRequest request) {
        logger.info("=== UserSyncController.createUserInBothDatabases() START - username: {} ===", user.getUsername());
        try {
            if (result.hasErrors()) {
                logger.warn("=== UserSyncController.createUserInBothDatabases() VALIDATION ERRORS ===");
                model.addAttribute("jobs", jobOptions);
                model.addAttribute("companies", companyOptions);
                return "users/sync/create";
            }
            
            // 두 DB에 사용자 저장
            userSyncService.saveToBothDatabases(user);

            // CommonDTO 생성 및 저장
            CommonDTO commonDTO = createCommonDTO(terminalId, terminalType, bankCode, branchCode, 
                                                channelType, userId, nation, ipAddress, request);
            commonDTO.setReqName("USER_REGISTER_SYNC");
            commonDTO.setUserID(user.getUsername());
            
            // CommonDTO 저장
            commonDC.saveCommonDTO(commonDTO);
            
            logger.info("=== UserSyncController.createUserInBothDatabases() END - User created in both databases and CommonDTO saved ===");
            return "redirect:/users/sync";
        } catch (Exception e) {
            logger.error("=== UserSyncController.createUserInBothDatabases() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자 수정 폼 표시
     */
    @GetMapping("/edit/{username}")
    public String showEditForm(@PathVariable String username, Model model) {
        logger.info("=== UserSyncController.showEditForm() START - username: {} ===", username);
        try {
            User user = userSyncService.findByUsernameFromBothDatabases(username);
            if (user == null) {
                logger.warn("=== UserSyncController.showEditForm() USER NOT FOUND ===");
                return "redirect:/users/sync";
            }
            
            model.addAttribute("user", user);
            model.addAttribute("jobs", jobOptions);
            model.addAttribute("companies", companyOptions);
            logger.info("=== UserSyncController.showEditForm() END ===");
            return "users/sync/edit";
        } catch (Exception e) {
            logger.error("=== UserSyncController.showEditForm() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자 정보를 두 DB에서 동시에 수정
     */
    @PostMapping("/update")
    public String updateUserInBothDatabases(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        logger.info("=== UserSyncController.updateUserInBothDatabases() START - username: {} ===", user.getUsername());
        try {
            if (result.hasErrors()) {
                logger.warn("=== UserSyncController.updateUserInBothDatabases() VALIDATION ERRORS ===");
                model.addAttribute("jobs", jobOptions);
                model.addAttribute("companies", companyOptions);
                return "users/sync/edit";
            }
            
            userSyncService.updateInBothDatabases(user);
            logger.info("=== UserSyncController.updateUserInBothDatabases() END - User updated in both databases ===");
            return "redirect:/users/sync";
        } catch (Exception e) {
            logger.error("=== UserSyncController.updateUserInBothDatabases() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자를 두 DB에서 동시에 삭제
     */
    @GetMapping("/delete/{username}")
    public String deleteUserFromBothDatabases(@PathVariable String username) {
        logger.info("=== UserSyncController.deleteUserFromBothDatabases() START - username: {} ===", username);
        try {
            userSyncService.deleteFromBothDatabases(username);
            logger.info("=== UserSyncController.deleteUserFromBothDatabases() END - User deleted from both databases ===");
            return "redirect:/users/sync";
        } catch (Exception e) {
            logger.error("=== UserSyncController.deleteUserFromBothDatabases() ERROR ===", e);
            throw e;
        }
    }

    /**
     * CommonDTO 생성 및 설정 (통합 사용자용)
     */
    private CommonDTO createCommonDTO(String terminalId, String terminalType, String bankCode, 
                                    String branchCode, String channelType, String userId, 
                                    String nation, String ipAddress, HttpServletRequest request) {
        CommonDTO commonDTO = new CommonDTO();
        
        // 필수 필드 설정
        commonDTO.setTerminalID(terminalId != null ? terminalId : "SYNC001");
        commonDTO.setTerminalType(terminalType != null ? terminalType : "WEB");
        commonDTO.setBankCode(bankCode != null ? bankCode : "999");
        commonDTO.setBranchCode(branchCode != null ? branchCode : "999");
        commonDTO.setChannelType(channelType != null ? channelType : "IB");
        commonDTO.setUserID(userId != null ? userId : "SYNC_SYSTEM");
        commonDTO.setNation(nation != null ? nation : "KR");
        
        // IP 주소 설정 (입력값 또는 실제 클라이언트 IP)
        String clientIP = ipAddress;
        if (clientIP == null || clientIP.isEmpty()) {
            clientIP = getClientIpAddr(request);
        }
        commonDTO.setIPAddress(clientIP);
        
        // 시스템 시간 및 날짜 설정
        LocalDateTime now = LocalDateTime.now();
        String currentDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String currentTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        
        commonDTO.setSystemDate(currentDate);
        commonDTO.setBusinessDate(currentDate);
        commonDTO.setSystemInTime(currentTime);
        
        // 기본값 설정 (통합 등록용)
        commonDTO.setTransactionNo(generateTransactionNo());
        commonDTO.setReqName("USER_REGISTER_SYNC");
        commonDTO.setTxcode("USR999");
        commonDTO.setUserLevel(1);
        commonDTO.setBaseCurrency("KRW");
        commonDTO.setTimeZone("GMT+9");
        commonDTO.setRegionCode("KR");
        
        return commonDTO;
    }

    /**
     * 클라이언트 IP 주소 추출
     */
    private String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 거래번호 생성 (통합 등록용)
     */
    private String generateTransactionNo() {
        return "SYNC" + System.currentTimeMillis();
    }
} 