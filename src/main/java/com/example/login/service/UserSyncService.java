package com.example.login.service;

import com.example.login.mapper.db1.UserMapperDb1;
import com.example.login.mapper.db2.UserMapperDb2;
import com.example.login.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
public class UserSyncService {

    private static final Logger logger = LoggerFactory.getLogger(UserSyncService.class);

    @Autowired
    private UserMapperDb1 userMapperDb1;
    
    @Autowired
    private UserMapperDb2 userMapperDb2;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 사용자를 두 DB에 동시에 저장
     */
    @Transactional
    public void saveToBothDatabases(User user) {
        logger.info("=== UserSyncService.saveToBothDatabases() START - username: {} ===", user.getUsername());
        try {
            // 비밀번호 암호화
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // DB1용 사용자 객체 생성 및 저장
            User userForDb1 = copyUser(user);
            userForDb1.setDbName("DB1");
            userMapperDb1.insert(userForDb1);
            logger.info("=== User saved to DB1 successfully with dbName: DB1 ===");
            
            // DB2용 사용자 객체 생성 및 저장
            User userForDb2 = copyUser(user);
            userForDb2.setDbName("DB2");
            userMapperDb2.insert(userForDb2);
            logger.info("=== User saved to DB2 successfully with dbName: DB2 ===");
            
            logger.info("=== UserSyncService.saveToBothDatabases() END - User saved to both databases ===");
        } catch (Exception e) {
            logger.error("=== UserSyncService.saveToBothDatabases() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자 정보를 두 DB에서 동시에 수정
     */
    @Transactional
    public void updateInBothDatabases(User user) {
        logger.info("=== UserSyncService.updateInBothDatabases() START - username: {} ===", user.getUsername());
        try {
            // 비밀번호 암호화
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // DB1용 사용자 객체 생성 및 수정
            User userForDb1 = copyUser(user);
            userForDb1.setDbName("DB1");
            userMapperDb1.update(userForDb1);
            logger.info("=== User updated in DB1 successfully with dbName: DB1 ===");
            
            // DB2용 사용자 객체 생성 및 수정
            User userForDb2 = copyUser(user);
            userForDb2.setDbName("DB2");
            userMapperDb2.update(userForDb2);
            logger.info("=== User updated in DB2 successfully with dbName: DB2 ===");
            
            logger.info("=== UserSyncService.updateInBothDatabases() END - User updated in both databases ===");
        } catch (Exception e) {
            logger.error("=== UserSyncService.updateInBothDatabases() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자를 두 DB에서 동시에 삭제
     */
    @Transactional
    public void deleteFromBothDatabases(String username) {
        logger.info("=== UserSyncService.deleteFromBothDatabases() START - username: {} ===", username);
        try {
            // DB1에서 삭제
            userMapperDb1.delete(username);
            logger.info("=== User deleted from DB1 successfully ===");
            
            // DB2에서 삭제
            userMapperDb2.delete(username);
            logger.info("=== User deleted from DB2 successfully ===");
            
            logger.info("=== UserSyncService.deleteFromBothDatabases() END - User deleted from both databases ===");
        } catch (Exception e) {
            logger.error("=== UserSyncService.deleteFromBothDatabases() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 두 DB의 사용자 목록을 모두 조회 (DB명 포함)
     */
    public List<User> findAllFromBothDatabases() {
        logger.info("=== UserSyncService.findAllFromBothDatabases() START ===");
        try {
            List<User> allUsers = new ArrayList<>();
            
            // DB1 사용자들 조회 및 DB명 설정
            List<User> db1Users = userMapperDb1.findAll();
            for (User user : db1Users) {
                user.setDbName("DB1");
                allUsers.add(user);
            }
            logger.info("=== Found {} users from DB1 ===", db1Users.size());
            
            // DB2 사용자들 조회 및 DB명 설정
            List<User> db2Users = userMapperDb2.findAll();
            for (User user : db2Users) {
                user.setDbName("DB2");
                allUsers.add(user);
            }
            logger.info("=== Found {} users from DB2 ===", db2Users.size());
            
            logger.info("=== UserSyncService.findAllFromBothDatabases() END - Found {} users total ===", allUsers.size());
            return allUsers;
        } catch (Exception e) {
            logger.error("=== UserSyncService.findAllFromBothDatabases() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 특정 사용자를 두 DB에서 찾기 (DB1 우선, DB명 포함)
     */
    public User findByUsernameFromBothDatabases(String username) {
        logger.info("=== UserSyncService.findByUsernameFromBothDatabases() START - username: {} ===", username);
        try {
            // DB1에서 먼저 찾기
            User user = userMapperDb1.findByUsername(username);
            if (user != null) {
                user.setDbName("DB1");
                logger.info("=== User found in DB1 with dbName set ===");
                return user;
            }
            
            // DB1에 없으면 DB2에서 찾기
            user = userMapperDb2.findByUsername(username);
            if (user != null) {
                user.setDbName("DB2");
                logger.info("=== User found in DB2 with dbName set ===");
            }
            
            logger.info("=== UserSyncService.findByUsernameFromBothDatabases() END - User found: {} ===", user != null);
            return user;
        } catch (Exception e) {
            logger.error("=== UserSyncService.findByUsernameFromBothDatabases() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자 객체 복사 유틸리티 메서드
     */
    private User copyUser(User original) {
        User copy = new User();
        copy.setUsername(original.getUsername());
        copy.setPassword(original.getPassword());
        copy.setAddress(original.getAddress());
        copy.setAge(original.getAge());
        copy.setJob(original.getJob());
        copy.setCompany(original.getCompany());
        copy.setToken(original.getToken());
        return copy;
    }
} 