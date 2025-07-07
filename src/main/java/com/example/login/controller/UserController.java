package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.UserService;
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
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CommonDC commonDC;

    private List<String> jobOptions = Arrays.asList("Developer", "Manager", "Designer", "Tester");
    private List<String> companyOptions = Arrays.asList("OpenAI", "Google", "Amazon", "Microsoft");

    @GetMapping
    public String listUsers(Model model) {
        logger.info("=== UserController.listUsers() START ===" + model.toString());
        try {
            List<User> users = userService.findAll();
            model.addAttribute("users", users);
            logger.info("=== UserController.listUsers() END - Found {} users ===", users.size());
            return "users/list";
        } catch (Exception e) {
            logger.error("=== UserController.listUsers() ERROR ===", e);
            throw e;
        }
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.info("=== UserController.showCreateForm() START ===");
        try {
            model.addAttribute("user", new User());
            model.addAttribute("jobs", jobOptions);
            model.addAttribute("companies", companyOptions);
            logger.info("=== UserController.showCreateForm() END ===");
            return "users/create";
        } catch (Exception e) {
            logger.error("=== UserController.showCreateForm() ERROR ===", e);
            throw e;
        }
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
        logger.info("=== UserController.createUser() START - username: {} ===", user.getUsername());
        try {
            if (result.hasErrors()) {
                logger.warn("=== UserController.createUser() VALIDATION ERRORS ===");
                model.addAttribute("jobs", jobOptions);
                model.addAttribute("companies", companyOptions);
                return "users/create";
            }

            // DB명 설정 및 사용자 저장
            user.setDbName("DB1");
            userService.save(user);

            // CommonDTO 생성 및 저장
            CommonDTO commonDTO = createCommonDTO(terminalId, terminalType, bankCode, branchCode, 
                                                channelType, userId, nation, ipAddress, request);
            commonDTO.setReqName("USER_REGISTER");
            commonDTO.setUserID(user.getUsername());
            
            // CommonDTO 저장
            commonDC.saveCommonDTO(commonDTO);
            
            logger.info("=== UserController.createUser() END - User and CommonDTO created successfully ===");
            return "redirect:/users";
        } catch (Exception e) {
            logger.error("=== UserController.createUser() ERROR ===", e);
            throw e;
        }
    }

    @GetMapping("/edit/{username}")
    public String showEditForm(@PathVariable String username, Model model) {
        logger.info("=== UserController.showEditForm() START - username: {} ===", username);
        try {
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
            model.addAttribute("jobs", jobOptions);
            model.addAttribute("companies", companyOptions);
            logger.info("=== UserController.showEditForm() END ===");
            return "users/edit";
        } catch (Exception e) {
            logger.error("=== UserController.showEditForm() ERROR ===", e);
            throw e;
        }
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        logger.info("=== UserController.updateUser() START - username: {} ===", user.getUsername());
        try {
            if (result.hasErrors()) {
                logger.warn("=== UserController.updateUser() VALIDATION ERRORS ===");
                model.addAttribute("jobs", jobOptions);
                model.addAttribute("companies", companyOptions);
                return "users/edit";
            }
            userService.update(user);
            logger.info("=== UserController.updateUser() END - User updated successfully ===");
            return "redirect:/users";
        } catch (Exception e) {
            logger.error("=== UserController.updateUser() ERROR ===", e);
            throw e;
        }
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username) {
        logger.info("=== UserController.deleteUser() START - username: {} ===", username);
        try {
            userService.delete(username);
            logger.info("=== UserController.deleteUser() END - User deleted successfully ===");
            return "redirect:/users";
        } catch (Exception e) {
            logger.error("=== UserController.deleteUser() ERROR ===", e);
            throw e;
        }
    }

    /**
     * CommonDTO 생성 및 설정
     */
    private CommonDTO createCommonDTO(String terminalId, String terminalType, String bankCode, 
                                    String branchCode, String channelType, String userId, 
                                    String nation, String ipAddress, HttpServletRequest request) {
        CommonDTO commonDTO = new CommonDTO();
        
        // 필수 필드 설정
        commonDTO.setTerminalID(terminalId != null ? terminalId : "WEB001");
        commonDTO.setTerminalType(terminalType != null ? terminalType : "WEB");
        commonDTO.setBankCode(bankCode != null ? bankCode : "001");
        commonDTO.setBranchCode(branchCode != null ? branchCode : "001");
        commonDTO.setChannelType(channelType != null ? channelType : "IB");
        commonDTO.setUserID(userId != null ? userId : "SYSTEM");
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
        
        // 기본값 설정
        commonDTO.setTransactionNo(generateTransactionNo());
        commonDTO.setReqName("USER_REGISTER");
        commonDTO.setTxcode("USR001");
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
     * 거래번호 생성
     */
    private String generateTransactionNo() {
        return "USR" + System.currentTimeMillis();
    }
}
