package com.example.login.deposite.transfer;

import com.example.login.deposite.business.dto.DepositeDDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * CDTO를 DDTO로 변환하는 유틸리티 클래스
 * 
 * 다양한 CDTO 타입들을 DepositeDDTO로 변환하는 기능을 제공합니다.
 */
@Component
public class CDTOToDDTOUtil {

    /**
     * DepositeCDTO를 DepositeDDTO 리스트로 변환
     * 
     * @param cdto DepositeCDTO 객체
     * @return DepositeDDTO 리스트
     */
    public List<DepositeDDTO> convertDepositeCDTOToDDTO(DepositeCDTO cdto) {
        List<DepositeDDTO> ddtoList = new ArrayList<>();
        
        if (cdto == null) {
            return ddtoList;
        }
        
        // 기존 ddto 리스트가 있으면 반환
        if (cdto.getDDtoList() != null && !cdto.getDDtoList().isEmpty()) {
            return new ArrayList<>(cdto.getDDtoList());
        }
        
        // CDTO의 기본 정보를 DDTO로 변환
        DepositeDDTO ddto = new DepositeDDTO();
        ddto.setId(cdto.getId());
        ddto.setName(cdto.getName());
        ddto.setCompany(cdto.getCompany());
        ddto.setAddress(cdto.getAddress());
        ddto.setContact(cdto.getContact());
        
        // 빈 값이 아닌 경우만 추가
        if (StringUtils.hasText(ddto.getId()) || 
            StringUtils.hasText(ddto.getName()) || 
            StringUtils.hasText(ddto.getCompany()) || 
            StringUtils.hasText(ddto.getAddress()) || 
            StringUtils.hasText(ddto.getContact())) {
            ddtoList.add(ddto);
        }
        
        return ddtoList;
    }

    /**
     * DEP80001CDTO를 DepositeDDTO 리스트로 변환
     * 
     * @param cdto DEP80001CDTO 객체
     * @return DepositeDDTO 리스트
     */
    public List<DepositeDDTO> convertDEP80001CDTOToDDTO(DEP80001CDTO cdto) {
        List<DepositeDDTO> ddtoList = new ArrayList<>();
        
        if (cdto == null) {
            return ddtoList;
        }
        
        // 기존 ddto 리스트가 있으면 반환
        if (cdto.getDdto() != null && !cdto.getDdto().isEmpty()) {
            return new ArrayList<>(cdto.getDdto());
        }
        
        // CDTO의 기본 정보를 DDTO로 변환
        DepositeDDTO ddto = new DepositeDDTO();
        ddto.setId(cdto.getId());
        ddto.setName(cdto.getName());
        ddto.setCompany(cdto.getCompany());
        ddto.setAddress(cdto.getAddress());
        ddto.setContact(cdto.getContact());
        
        // 빈 값이 아닌 경우만 추가
        if (StringUtils.hasText(ddto.getId()) || 
            StringUtils.hasText(ddto.getName()) || 
            StringUtils.hasText(ddto.getCompany()) || 
            StringUtils.hasText(ddto.getAddress()) || 
            StringUtils.hasText(ddto.getContact())) {
            ddtoList.add(ddto);
        }
        
        return ddtoList;
    }

    /**
     * CommonDTO 정보를 포함하여 DepositeCDTO를 DepositeDDTO로 변환
     * CommonDTO 정보도 함께 고려하여 더 풍부한 DDTO를 생성
     * 
     * @param cdto DepositeCDTO 객체
     * @return DepositeDDTO 리스트
     */
    public List<DepositeDDTO> convertDepositeCDTOWithCommonToDDTO(DepositeCDTO cdto) {
        List<DepositeDDTO> ddtoList = convertDepositeCDTOToDDTO(cdto);
        
        if (cdto != null && cdto.getComDTO() != null) {
            CommonDTO comDTO = cdto.getComDTO();
            
            // CommonDTO에서 추가 정보를 가져와서 DDTO 보강
            for (DepositeDDTO ddto : ddtoList) {
                // CommonDTO의 userID를 id가 없는 경우에 사용
                if (!StringUtils.hasText(ddto.getId()) && StringUtils.hasText(comDTO.getUserID())) {
                    ddto.setId(comDTO.getUserID());
                }
                
                // CommonDTO의 정보를 address에 추가 (기존 정보가 없는 경우)
                if (!StringUtils.hasText(ddto.getAddress()) && StringUtils.hasText(comDTO.getIPAddress())) {
                    ddto.setAddress("IP: " + comDTO.getIPAddress());
                }
                
                // CommonDTO의 terminalID를 contact에 추가 (기존 정보가 없는 경우)
                if (!StringUtils.hasText(ddto.getContact()) && StringUtils.hasText(comDTO.getTerminalID())) {
                    ddto.setContact("Terminal: " + comDTO.getTerminalID());
                }
            }
        }
        
        return ddtoList;
    }

    /**
     * 여러 CDTO를 하나의 DDTO 리스트로 병합
     * 
     * @param depositeCDTOs DepositeCDTO 배열
     * @return 병합된 DepositeDDTO 리스트
     */
    public List<DepositeDDTO> mergeCDTOsToDDTOList(DepositeCDTO... depositeCDTOs) {
        List<DepositeDDTO> mergedList = new ArrayList<>();
        
        if (depositeCDTOs != null) {
            for (DepositeCDTO cdto : depositeCDTOs) {
                List<DepositeDDTO> convertedList = convertDepositeCDTOToDDTO(cdto);
                mergedList.addAll(convertedList);
            }
        }
        
        return mergedList;
    }

    /**
     * DDTO 리스트를 필터링 (빈 값 제거)
     * 
     * @param ddtoList DepositeDDTO 리스트
     * @return 필터링된 DepositeDDTO 리스트
     */
    public List<DepositeDDTO> filterEmptyDDTOs(List<DepositeDDTO> ddtoList) {
        List<DepositeDDTO> filteredList = new ArrayList<>();
        
        if (ddtoList != null) {
            for (DepositeDDTO ddto : ddtoList) {
                if (ddto != null && 
                    (StringUtils.hasText(ddto.getId()) || 
                     StringUtils.hasText(ddto.getName()) || 
                     StringUtils.hasText(ddto.getCompany()) || 
                     StringUtils.hasText(ddto.getAddress()) || 
                     StringUtils.hasText(ddto.getContact()))) {
                    filteredList.add(ddto);
                }
            }
        }
        
        return filteredList;
    }

    /**
     * DDTO에서 특정 필드를 기준으로 중복 제거
     * 
     * @param ddtoList DepositeDDTO 리스트
     * @param key 중복 제거 기준 ("id", "name", "company" 등)
     * @return 중복이 제거된 DepositeDDTO 리스트
     */
    public List<DepositeDDTO> removeDuplicatesByKey(List<DepositeDDTO> ddtoList, String key) {
        List<DepositeDDTO> uniqueList = new ArrayList<>();
        List<String> seenValues = new ArrayList<>();
        
        if (ddtoList != null && StringUtils.hasText(key)) {
            for (DepositeDDTO ddto : ddtoList) {
                if (ddto != null) {
                    String value = getFieldValue(ddto, key);
                    
                    if (StringUtils.hasText(value) && !seenValues.contains(value)) {
                        seenValues.add(value);
                        uniqueList.add(ddto);
                    } else if (!StringUtils.hasText(value)) {
                        // null이나 빈 값인 경우도 포함
                        uniqueList.add(ddto);
                    }
                }
            }
        }
        
        return uniqueList;
    }

    /**
     * DepositeDDTO에서 특정 필드 값을 가져오는 헬퍼 메서드
     */
    private String getFieldValue(DepositeDDTO ddto, String fieldName) {
        switch (fieldName.toLowerCase()) {
            case "id":
                return ddto.getId();
            case "name":
                return ddto.getName();
            case "company":
                return ddto.getCompany();
            case "address":
                return ddto.getAddress();
            case "contact":
                return ddto.getContact();
            default:
                return null;
        }
    }
} 