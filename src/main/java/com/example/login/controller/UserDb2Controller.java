package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.UserDb2Service;
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
@RequestMapping("/users/db2")
public class UserDb2Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(UserDb2Controller.class);
    
    @Autowired
    private UserDb2Service userService;

    @Autowired
    private CommonDC commonDC;

    private List<String> jobOptions = Arrays.asList("Developer", "Manager", "Designer", "Tester");
    private List<String> companyOptions = Arrays.asList("OpenAI", "Google", "Amazon", "Microsoft");

    @GetMapping
    public String listUsers(Model model) {
        logger.info("=== UserDb2Controller.listUsers() START ===");
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        logger.info("=== UserDb2Controller.listUsers() END - Found {} users ===", users.size());
        return "users/db2/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.info("=== UserDb2Controller.showCreateForm() START ===");
        model.addAttribute("user", new User());
        model.addAttribute("jobs", jobOptions);
        model.addAttribute("companies", companyOptions);
        logger.info("=== UserDb2Controller.showCreateForm() END ===");
        return "users/db2/create";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute User user, BindingResult result, 
                           @RequestParam(required = false) String terminalId,
                           @RequestParam(required = false) String terminalType,
                           @RequestParam(required = false) String bankCode,
                           @RequestParam(required = false) String branchCode,
                           @RequestParam(required = false) String channelType,
                           @RequestParam(required = false) String userId,
                           @RequestParam(required = false) String nation,
                           @RequestParam(required = false) String ipAddress,
                           Model model, HttpServletRequest request) {
        logger.info("=== UserDb2Controller.createUser() START - username: {} ===", user.getUsername());
        try {
            if (result.hasErrors()) {
                logger.warn("=== UserDb2Controller.createUser() VALIDATION ERRORS ===");
                model.addAttribute("jobs", jobOptions);
                model.addAttribute("companies", companyOptions);
                return "users/db2/create";
            }

            // DB명 설정 및 DB2에 사용자 저장
            user.setDbName("DB2");
            userService.save(user);

            // CommonDTO 생성 및 저장
            CommonDTO commonDTO = createCommonDTO(terminalId, terminalType, bankCode, branchCode, 
                                                channelType, userId, nation, ipAddress, request);
            commonDTO.setReqName("USER_REGISTER_DB2");
            commonDTO.setUserID(user.getUsername());
            
            // CommonDTO 저장
            commonDC.saveCommonDTO(commonDTO);
            
            logger.info("=== UserDb2Controller.createUser() END - User and CommonDTO created successfully ===");
            return "redirect:/users/db2";
        } catch (Exception e) {
            logger.error("=== UserDb2Controller.createUser() ERROR ===", e);
            throw e;
        }
    }

    @GetMapping("/edit/{username}")
    public String showEditForm(@PathVariable String username, Model model) {
        logger.info("=== UserDb2Controller.showEditForm() START - username: {} ===", username);
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("jobs", jobOptions);
        model.addAttribute("companies", companyOptions);
        logger.info("=== UserDb2Controller.showEditForm() END ===");
        return "users/db2/edit";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        logger.info("=== UserDb2Controller.updateUser() START - username: {} ===", user.getUsername());
        if (result.hasErrors()) {
            logger.warn("=== UserDb2Controller.updateUser() VALIDATION ERRORS ===");
            model.addAttribute("jobs", jobOptions);
            model.addAttribute("companies", companyOptions);
            return "users/db2/edit";
        }
        userService.update(user);
        logger.info("=== UserDb2Controller.updateUser() END ===");
        return "redirect:/users/db2";
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username) {
        logger.info("=== UserDb2Controller.deleteUser() START - username: {} ===", username);
        userService.delete(username);
        logger.info("=== UserDb2Controller.deleteUser() END ===");
        return "redirect:/users/db2";
    }

    /**
     * CommonDTO 생성 및 설정 (DB2 전용)
     */
    private CommonDTO createCommonDTO(String terminalId, String terminalType, String bankCode, 
                                    String branchCode, String channelType, String userId, 
                                    String nation, String ipAddress, HttpServletRequest request) {
        CommonDTO commonDTO = new CommonDTO();
        
        // 필수 필드 설정
        commonDTO.setTerminalID(terminalId != null ? terminalId : "DB2001");
        commonDTO.setTerminalType(terminalType != null ? terminalType : "WEB");
        commonDTO.setBankCode(bankCode != null ? bankCode : "002");
        commonDTO.setBranchCode(branchCode != null ? branchCode : "002");
        commonDTO.setChannelType(channelType != null ? channelType : "IB");
        commonDTO.setUserID(userId != null ? userId : "DB2_SYSTEM");
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
        
        // 기본값 설정 (DB2 전용)
        commonDTO.setTransactionNo(generateTransactionNo());
        commonDTO.setReqName("USER_REGISTER_DB2");
        commonDTO.setTxcode("USR002");
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
     * 거래번호 생성 (DB2 전용)
     */
    private String generateTransactionNo() {
        return "DB2" + System.currentTimeMillis();
    }
} 