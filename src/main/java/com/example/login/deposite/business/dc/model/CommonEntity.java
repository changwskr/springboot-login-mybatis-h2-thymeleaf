package com.example.login.deposite.business.dc.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 공통 정보 엔티티
 * 
 * 터미널 정보, 거래 정보, 시스템 정보 등을 DB에 저장하기 위한 JPA Entity 클래스입니다.
 */
@Entity
@Table(name = "COMMON_INFO")
public class CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 터미널 정보
    @Column(name = "TERMINAL_ID", length = 20)
    private String terminalID;

    @Column(name = "TERMINAL_TYPE", length = 10)
    private String terminalType;

    @Column(name = "XML_SEQ", length = 20)
    private String xmlSeq;

    @Column(name = "BANK_CODE", length = 10)
    private String bankCode;

    @Column(name = "BRANCH_CODE", length = 10)
    private String branchCode;

    @Column(name = "GL_POST_BRANCH_CODE", length = 10)
    private String glPostBranchCode;

    @Column(name = "CHANNEL_TYPE", length = 10)
    private String channelType;

    @Column(name = "USER_ID", length = 50)
    private String userID;

    @Column(name = "EVENT_NO", length = 20)
    private String eventNo;

    @Column(name = "NATION", length = 5)
    private String nation;

    @Column(name = "REGION_CODE", length = 10)
    private String regionCode;

    @Column(name = "TIME_ZONE", length = 10)
    private String timeZone;

    @Column(name = "FX_RATE_COUNT")
    private Integer fxRateCount;

    @Column(name = "REQ_NAME", length = 50)
    private String reqName;

    @Column(name = "SYSTEM_DATE", length = 8)
    private String systemDate;

    @Column(name = "BUSINESS_DATE", length = 8)
    private String businessDate;

    @Column(name = "SYSTEM_IN_TIME", length = 14)
    private String systemInTime;

    @Column(name = "SYSTEM_OUT_TIME", length = 14)
    private String systemOutTime;

    @Column(name = "TRANSACTION_NO", length = 30)
    private String transactionNo;

    @Column(name = "BASE_CURRENCY", length = 3)
    private String baseCurrency;

    @Column(name = "MULTI_PL", length = 10)
    private String multiPL;

    @Column(name = "USER_LEVEL")
    private Integer userLevel;

    @Column(name = "IP_ADDRESS", length = 15)
    private String ipAddress;

    @Column(name = "TX_CODE", length = 10)
    private String txcode;

    // 감사 필드
    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    // JPA 생명주기 메서드
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }

    // 생성자
    public CommonEntity() {
    }

    public CommonEntity(String terminalID, String userID, String transactionNo) {
        this.terminalID = terminalID;
        this.userID = userID;
        this.transactionNo = transactionNo;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getXmlSeq() {
        return xmlSeq;
    }

    public void setXmlSeq(String xmlSeq) {
        this.xmlSeq = xmlSeq;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getGlPostBranchCode() {
        return glPostBranchCode;
    }

    public void setGlPostBranchCode(String glPostBranchCode) {
        this.glPostBranchCode = glPostBranchCode;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Integer getFxRateCount() {
        return fxRateCount;
    }

    public void setFxRateCount(Integer fxRateCount) {
        this.fxRateCount = fxRateCount;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public String getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(String systemDate) {
        this.systemDate = systemDate;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public String getSystemInTime() {
        return systemInTime;
    }

    public void setSystemInTime(String systemInTime) {
        this.systemInTime = systemInTime;
    }

    public String getSystemOutTime() {
        return systemOutTime;
    }

    public void setSystemOutTime(String systemOutTime) {
        this.systemOutTime = systemOutTime;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getMultiPL() {
        return multiPL;
    }

    public void setMultiPL(String multiPL) {
        this.multiPL = multiPL;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getTxcode() {
        return txcode;
    }

    public void setTxcode(String txcode) {
        this.txcode = txcode;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "CommonEntity{" +
                "id=" + id +
                ", terminalID='" + terminalID + '\'' +
                ", userID='" + userID + '\'' +
                ", transactionNo='" + transactionNo + '\'' +
                ", reqName='" + reqName + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
} 