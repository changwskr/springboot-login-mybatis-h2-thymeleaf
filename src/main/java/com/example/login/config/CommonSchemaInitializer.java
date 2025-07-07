package com.example.login.config;

import com.example.login.deposite.business.dc.dao.CommonRepository;
import com.example.login.deposite.business.dc.CommonDC;
import com.example.login.deposite.business.dc.model.CommonEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * Common 테이블 스키마 초기화
 * 
 * 애플리케이션 기동시 Common 테이블을 확인하고 기본 데이터를 삽입합니다.
 */
@Component
public class CommonSchemaInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CommonSchemaInitializer.class);

    @Autowired
    private CommonRepository commonRepository;
    
    @Autowired
    private CommonDC commonDC;

    @Autowired
    @Qualifier("db1DataSource")
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        log.info("[INIT] Common 테이블 초기화 시작");

        try {
            // 테이블 존재 여부 확인
            if (!isTableExists("COMMON_INFO")) {
                log.info("[INIT] COMMON_INFO 테이블이 존재하지 않음. JPA DDL 완료 대기 중...");
                // JPA DDL이 완료될 때까지 대기
                int maxRetries = 10;
                for (int i = 0; i < maxRetries; i++) {
                    Thread.sleep(2000); // 2초 대기
                    if (isTableExists("COMMON_INFO")) {
                        log.info("[INIT] COMMON_INFO 테이블 생성 확인됨");
                        break;
                    }
                    log.info("[INIT] COMMON_INFO 테이블 대기 중... ({}/{})", i + 1, maxRetries);
                }
            }

            // 데이터 삽입 시도
            insertDataIfNotExists();

        } catch (Exception e) {
            log.error("[ERROR] Common 테이블 초기화 중 오류 발생: {}", e.getMessage(), e);
            // 초기화 실패해도 애플리케이션 구동은 계속 진행
        }
    }

    private boolean isTableExists(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getTables(null, null, tableName, new String[]{"TABLE"})) {
                return resultSet.next();
            }
        } catch (Exception e) {
            log.warn("[INIT] 테이블 존재 여부 확인 중 오류: {}", e.getMessage());
            return false;
        }
    }

    @Transactional(transactionManager = "jpaTransactionManager")
    public void insertDataIfNotExists() {
        try {
            // 기존 데이터 확인
            long count = commonRepository.count();
            log.info("[INIT] 기존 Common 데이터 개수: {}", count);

            if (count == 0) {
                log.info("[INIT] 기본 Common 데이터 삽입 시작");
                insertDefaultData();
                log.info("[INIT] 기본 Common 데이터 삽입 완료");
            } else {
                log.info("[INIT] 기존 데이터가 존재하므로 초기화를 건너뜁니다.");
            }

            log.info("[INIT] Common 테이블 초기화 완료");
        } catch (Exception e) {
            log.error("[ERROR] 데이터 삽입 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    /**
     * 기본 데이터 삽입
     */
    public void insertDefaultData() {
        // 기본 터미널 정보
        CommonEntity defaultCommon1 = new CommonEntity();
        defaultCommon1.setTerminalID("TERM001");
        defaultCommon1.setTerminalType("WEB");
        defaultCommon1.setBankCode("001");
        defaultCommon1.setBranchCode("001");
        defaultCommon1.setChannelType("WEB");
        defaultCommon1.setUserID("SYSTEM");
        defaultCommon1.setNation("KR");
        defaultCommon1.setRegionCode("01");
        defaultCommon1.setTimeZone("GMT+9");
        defaultCommon1.setFxRateCount(0);
        defaultCommon1.setReqName("SYSTEM_INIT");
        defaultCommon1.setBaseCurrency("KRW");
        defaultCommon1.setUserLevel(9);
        defaultCommon1.setIpAddress("127.0.0.1");
        defaultCommon1.setTxcode("INIT");
        defaultCommon1.setTransactionNo("INIT_" + System.currentTimeMillis());

        // 모바일 터미널 정보
        CommonEntity defaultCommon2 = new CommonEntity();
        defaultCommon2.setTerminalID("TERM002");
        defaultCommon2.setTerminalType("MOBILE");
        defaultCommon2.setBankCode("001");
        defaultCommon2.setBranchCode("002");
        defaultCommon2.setChannelType("MOBILE");
        defaultCommon2.setUserID("MOBILE_SYS");
        defaultCommon2.setNation("KR");
        defaultCommon2.setRegionCode("02");
        defaultCommon2.setTimeZone("GMT+9");
        defaultCommon2.setFxRateCount(0);
        defaultCommon2.setReqName("MOBILE_INIT");
        defaultCommon2.setBaseCurrency("KRW");
        defaultCommon2.setUserLevel(8);
        defaultCommon2.setIpAddress("192.168.1.1");
        defaultCommon2.setTxcode("M_INIT");
        defaultCommon2.setTransactionNo("MOBILE_" + System.currentTimeMillis());

        // ATM 터미널 정보
        CommonEntity defaultCommon3 = new CommonEntity();
        defaultCommon3.setTerminalID("TERM003");
        defaultCommon3.setTerminalType("ATM");
        defaultCommon3.setBankCode("001");
        defaultCommon3.setBranchCode("003");
        defaultCommon3.setChannelType("ATM");
        defaultCommon3.setUserID("ATM_SYS");
        defaultCommon3.setNation("KR");
        defaultCommon3.setRegionCode("03");
        defaultCommon3.setTimeZone("GMT+9");
        defaultCommon3.setFxRateCount(0);
        defaultCommon3.setReqName("ATM_INIT");
        defaultCommon3.setBaseCurrency("KRW");
        defaultCommon3.setUserLevel(7);
        defaultCommon3.setIpAddress("10.0.0.1");
        defaultCommon3.setTxcode("ATM_INIT");
        defaultCommon3.setTransactionNo("ATM_" + System.currentTimeMillis());

        // 데이터 저장
        commonRepository.save(defaultCommon1);
        commonRepository.save(defaultCommon2);
        commonRepository.save(defaultCommon3);

        log.info("[INIT] 3개의 기본 Common 데이터가 삽입되었습니다.");
    }
} 