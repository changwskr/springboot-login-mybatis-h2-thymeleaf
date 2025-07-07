package com.example.login.deposite.transfer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 공통 데이터 전송 객체
 * 
 * 터미널 정보, 거래 정보, 시스템 정보 등을 포함하는 공통 DTO 클래스입니다.
 * 은행 시스템에서 사용되는 표준적인 공통 필드들을 정의합니다.
 */
public class CommonDTO {

    // 터미널 정보
    private String terminalID;           // 터미널 ID
    private String terminalType;         // 터미널 타입
    private String xmlSeq;               // XML 시퀀스 번호
    private String bankCode;             // 은행 코드
    private String branchCode;           // 지점 코드
    private String glPostBranchCode;     // GL 포스트 지점 코드
    private String channelType;          // 채널 타입
    private String userID;               // 사용자 ID
    private String eventNo;              // 이벤트 번호
    private String nation;               // 국가 코드
    private String regionCode;           // 지역 코드
    private String timeZone;             // 시간대
    private int fxRateCount;             // 환율 카운트
    private String reqName;              // 요청명
    private String systemDate;           // 시스템 날짜
    private String businessDate;         // 업무 날짜
    private String systemInTime;         // 시스템 입장 시간
    private String systemOutTime;        // 시스템 퇴장 시간
    private String transactionNo;        // 거래 번호
    private String baseCurrency;         // 기준 통화
    private String multiPL;              // 다국어 설정
    private int userLevel;               // 사용자 레벨
    private String IPAddress;            // IP 주소
    private String txcode;               // 거래 코드

    // 생성자
    public CommonDTO() {
        // 기본 생성자
    }

    public CommonDTO(String terminalID, String userID, String transactionNo) {
        this.terminalID = terminalID;
        this.userID = userID;
        this.transactionNo = transactionNo;
    }

    // Getters and Setters
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

    public int getFxRateCount() {
        return fxRateCount;
    }

    public void setFxRateCount(int fxRateCount) {
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

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getTxcode() {
        return txcode;
    }

    public void setTxcode(String txcode) {
        this.txcode = txcode;
    }

    /**
     * 시스템 입장 시간을 현재 시간으로 설정
     * 형식: yyyyMMddHHmmss (14자)
     */
    public void setSystemInTimeNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.systemInTime = LocalDateTime.now().format(formatter);
    }

    /**
     * 시스템 퇴장 시간을 현재 시간으로 설정
     * 형식: yyyyMMddHHmmss (14자)
     */
    public void setSystemOutTimeNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.systemOutTime = LocalDateTime.now().format(formatter);
    }

    /**
     * 시스템 날짜를 현재 날짜로 설정
     * 형식: yyyyMMdd (8자)
     */
    public void setSystemDateNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.systemDate = LocalDateTime.now().format(formatter);
    }

    /**
     * 업무 날짜를 현재 날짜로 설정
     * 형식: yyyyMMdd (8자)
     */
    public void setBusinessDateNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.businessDate = LocalDateTime.now().format(formatter);
    }

    @Override
    public String toString() {
        return "CommonDTO{" +
                "terminalID='" + terminalID + '\'' +
                ", terminalType='" + terminalType + '\'' +
                ", xmlSeq='" + xmlSeq + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", branchCode='" + branchCode + '\'' +
                ", glPostBranchCode='" + glPostBranchCode + '\'' +
                ", channelType='" + channelType + '\'' +
                ", userID='" + userID + '\'' +
                ", eventNo='" + eventNo + '\'' +
                ", nation='" + nation + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", fxRateCount=" + fxRateCount +
                ", reqName='" + reqName + '\'' +
                ", systemDate='" + systemDate + '\'' +
                ", businessDate='" + businessDate + '\'' +
                ", systemInTime='" + systemInTime + '\'' +
                ", systemOutTime='" + systemOutTime + '\'' +
                ", transactionNo='" + transactionNo + '\'' +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", multiPL='" + multiPL + '\'' +
                ", userLevel=" + userLevel +
                ", IPAddress='" + IPAddress + '\'' +
                ", txcode='" + txcode + '\'' +
                '}';
    }
} 