package com.example.login.deposite.transfer;

import com.example.login.deposite.business.dto.DepositeDDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 범용 CDTO를 DDTO로 변환하는 유틸리티 클래스
 * 
 * 런타임에 DDTO 객체를 분석하여 필드를 파악하고,
 * CDTO에서 같은 이름의 필드가 있으면 자동으로 매핑하는 기능을 제공합니다.
 */
@Component
public class UniversalCDTOToDDTOUtil {

    private static final Logger logger = LoggerFactory.getLogger(UniversalCDTOToDDTOUtil.class);

    /**
     * 범용 CDTO → DDTO 변환 API
     * 
     * @param cdto 변환할 CDTO 객체 (어떤 클래스든 가능)
     * @return DepositeDDTO 리스트
     */
    public List<DepositeDDTO> convertToDDTO(Object cdto) {
        logger.info("=== UniversalCDTOToDDTOUtil.convertToDDTO() START ===");
        List<DepositeDDTO> ddtoList = new ArrayList<>();
        
        if (cdto == null) {
            logger.warn("CDTO 객체가 null입니다.");
            return ddtoList;
        }
        
        logger.info("변환 대상 CDTO 클래스: {}", cdto.getClass().getSimpleName());
        
        try {
            // 1. 먼저 기존 ddto 리스트가 있는지 확인
            List<DepositeDDTO> existingDdtoList = extractExistingDdtoList(cdto);
            if (existingDdtoList != null && !existingDdtoList.isEmpty()) {
                logger.info("기존 ddto 리스트 발견: {}개", existingDdtoList.size());
                return new ArrayList<>(existingDdtoList);
            }
            
            // 2. 새로운 DDTO 객체를 생성하고 런타임 분석하여 매핑
            DepositeDDTO mappedDdto = createDdtoFromCdtoByReflection(cdto);
            
            // 3. 유효한 데이터가 있는 경우만 추가
            if (isValidDdto(mappedDdto)) {
                ddtoList.add(mappedDdto);
                logger.info("CDTO → DDTO 변환 완료: {}", mappedDdto);
            } else {
                logger.warn("변환된 DDTO에 유효한 데이터가 없습니다.");
            }
            
        } catch (Exception e) {
            logger.error("CDTO → DDTO 변환 중 오류 발생: {}", e.getMessage(), e);
        }
        
        logger.info("=== UniversalCDTOToDDTOUtil.convertToDDTO() END - 결과: {}개 ===", ddtoList.size());
        return ddtoList;
    }

    /**
     * CDTO에서 기존 ddto 리스트를 추출 (이미 변환된 DDTO가 있는 경우)
     */
    @SuppressWarnings("unchecked")
    private List<DepositeDDTO> extractExistingDdtoList(Object cdto) {
        try {
            // "ddto" 필드를 찾아서 반환
            Object ddtoValue = getCdtoFieldValue(cdto, "ddto");
            if (ddtoValue instanceof List) {
                List<?> list = (List<?>) ddtoValue;
                if (!list.isEmpty() && list.get(0) instanceof DepositeDDTO) {
                    return (List<DepositeDDTO>) ddtoValue;
                }
            }
        } catch (Exception e) {
            logger.debug("기존 ddto 리스트 추출 실패: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 런타임에 DDTO 객체를 분석하여 필드를 파악하고, CDTO에서 같은 이름의 필드를 찾아서 매핑
     */
    private DepositeDDTO createDdtoFromCdtoByReflection(Object cdto) {
        DepositeDDTO ddto = new DepositeDDTO();
        
        logger.debug("DDTO 객체 런타임 분석 및 CDTO 매핑 시작");
        
        // 1. DDTO 객체의 모든 필드를 런타임에 분석
        String[] ddtoFieldNames = analyzeDdtoFields(ddto);
        logger.info("DDTO 분석 결과 - 매핑 대상 필드: {}", Arrays.toString(ddtoFieldNames));
        
        // 2. DDTO의 각 필드에 대해 CDTO에서 같은 이름의 필드를 찾아서 값 설정
        for (String fieldName : ddtoFieldNames) {
            try {
                // CDTO에서 해당 필드명의 값을 가져옴
                Object fieldValue = getCdtoFieldValue(cdto, fieldName);
                
                if (fieldValue != null && StringUtils.hasText(fieldValue.toString())) {
                    String stringValue = fieldValue.toString();
                    
                    // DDTO의 해당 필드에 값 설정
                    setDdtoFieldByReflection(ddto, fieldName, stringValue);
                    logger.debug("필드 매핑 성공: {} = '{}'", fieldName, stringValue);
                } else {
                    logger.debug("필드 값이 비어있음: {}", fieldName);
                }
                
            } catch (Exception e) {
                logger.debug("필드 매핑 실패 ({}): {}", fieldName, e.getMessage());
            }
        }
        
        return ddto;
    }

    /**
     * 런타임에 DDTO 객체를 분석하여 모든 필드명을 추출
     */
    private String[] analyzeDdtoFields(DepositeDDTO ddto) {
        List<String> fieldNames = new ArrayList<>();
        Class<?> ddtoClass = ddto.getClass();
        
        logger.debug("DDTO 클래스 분석: {}", ddtoClass.getSimpleName());
        
        // DDTO 클래스의 모든 필드를 가져옴 (상속받은 필드 포함)
        Field[] fields = getAllFields(ddtoClass);
        
        for (Field field : fields) {
            String fieldName = field.getName();
            
            // static, final 필드는 제외
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
                java.lang.reflect.Modifier.isFinal(field.getModifiers()) ||
                fieldName.equals("serialVersionUID")) {
                continue;
            }
            
            fieldNames.add(fieldName);
            logger.debug("DDTO 필드 발견: {} (타입: {})", fieldName, field.getType().getSimpleName());
        }
        
        return fieldNames.toArray(new String[0]);
    }

    /**
     * 클래스 계층구조를 포함하여 모든 필드를 가져오는 메서드
     */
    private Field[] getAllFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = clazz;
        
        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            allFields.addAll(Arrays.asList(fields));
            currentClass = currentClass.getSuperclass();
        }
        
        return allFields.toArray(new Field[0]);
    }

    /**
     * CDTO 객체에서 특정 필드의 값을 가져오는 메서드
     */
    private Object getCdtoFieldValue(Object cdto, String fieldName) {
        Class<?> cdtoClass = cdto.getClass();
        
        // 1. Getter 메서드로 시도 (getFieldName, getfieldname, isFieldName 등)
        String[] getterPatterns = {
            "get" + capitalize(fieldName),           // getId
            "get" + fieldName.toLowerCase(),         // getid  
            "is" + capitalize(fieldName),            // isId (boolean용)
            fieldName                                // id (직접 접근)
        };
        
        for (String getterName : getterPatterns) {
            try {
                Method getter = findMethod(cdtoClass, getterName);
                if (getter != null) {
                    getter.setAccessible(true);
                    Object value = getter.invoke(cdto);
                    logger.debug("Getter 메서드로 값 획득: {}() = {}", getterName, value);
                    return value;
                }
            } catch (Exception e) {
                logger.debug("Getter 메서드 실패 ({}): {}", getterName, e.getMessage());
            }
        }
        
        // 2. 직접 필드 접근으로 시도
        try {
            Field field = findField(cdtoClass, fieldName);
            if (field != null) {
                field.setAccessible(true);
                Object value = field.get(cdto);
                logger.debug("직접 필드 접근으로 값 획득: {} = {}", fieldName, value);
                return value;
            }
        } catch (Exception e) {
            logger.debug("직접 필드 접근 실패 ({}): {}", fieldName, e.getMessage());
        }
        
        logger.debug("필드 값을 찾을 수 없음: {}", fieldName);
        return null;
    }

    /**
     * 리플렉션을 사용하여 DDTO의 특정 필드에 값을 설정
     */
    private void setDdtoFieldByReflection(DepositeDDTO ddto, String fieldName, String value) {
        try {
            // 1. Setter 메서드로 시도
            String setterName = "set" + capitalize(fieldName);
            Method setter = findMethod(ddto.getClass(), setterName, String.class);
            
            if (setter != null) {
                setter.setAccessible(true);
                setter.invoke(ddto, value);
                logger.debug("Setter 메서드로 값 설정: {}('{}') 성공", setterName, value);
                return;
            }
            
            // 2. 직접 필드 접근으로 시도
            Field field = findField(ddto.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(ddto, value);
                logger.debug("직접 필드 접근으로 값 설정: {} = '{}' 성공", fieldName, value);
                return;
            }
            
            logger.warn("DDTO 필드 설정 실패: {} (메서드와 필드 모두 찾을 수 없음)", fieldName);
            
        } catch (Exception e) {
            logger.error("DDTO 필드 설정 중 오류 ({}): {}", fieldName, e.getMessage(), e);
        }
    }

    /**
     * 클래스 계층구조에서 필드를 찾는 메서드
     */
    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // 부모 클래스에서 계속 찾기
                currentClass = currentClass.getSuperclass();
            }
        }
        
        return null;
    }

    /**
     * 클래스 계층구조에서 메서드를 찾는 메서드 (파라미터 없음)
     */
    private Method findMethod(Class<?> clazz, String methodName) {
        Class<?> currentClass = clazz;
        
        while (currentClass != null && currentClass != Object.class) {
            try {
                // 파라미터가 없는 메서드를 찾음 (getter)
                return currentClass.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                // 부모 클래스에서 계속 찾기
                currentClass = currentClass.getSuperclass();
            }
        }
        
        return null;
    }

    /**
     * 클래스 계층구조에서 메서드를 찾는 메서드 (파라미터 타입 지정)
     */
    private Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Class<?> currentClass = clazz;
        
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                // 부모 클래스에서 계속 찾기
                currentClass = currentClass.getSuperclass();
            }
        }
        
        return null;
    }

    /**
     * 문자열의 첫 글자를 대문자로 변환
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * DDTO가 유효한 데이터를 가지고 있는지 확인
     */
    private boolean isValidDdto(DepositeDDTO ddto) {
        if (ddto == null) {
            return false;
        }
        
        // DDTO의 모든 필드를 런타임에 검사
        Field[] fields = getAllFields(ddto.getClass());
        
        for (Field field : fields) {
            // static, final 필드는 제외
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
                java.lang.reflect.Modifier.isFinal(field.getModifiers()) ||
                field.getName().equals("serialVersionUID")) {
                continue;
            }
            
            try {
                field.setAccessible(true);
                Object value = field.get(ddto);
                
                if (value != null && StringUtils.hasText(value.toString())) {
                    return true; // 하나라도 유효한 값이 있으면 true
                }
            } catch (Exception e) {
                logger.debug("필드 유효성 검사 실패 ({}): {}", field.getName(), e.getMessage());
            }
        }
        
        return false;
    }

    /**
     * 여러 CDTO 객체를 한 번에 변환
     * 
     * @param cdtos 변환할 CDTO 객체들
     * @return 병합된 DepositeDDTO 리스트
     */
    public List<DepositeDDTO> convertMultipleToDDTO(Object... cdtos) {
        logger.info("=== 다중 CDTO 변환 시작: {}개 ===", cdtos != null ? cdtos.length : 0);
        
        List<DepositeDDTO> allDdtoList = new ArrayList<>();
        
        if (cdtos != null) {
            for (Object cdto : cdtos) {
                try {
                    List<DepositeDDTO> convertedList = convertToDDTO(cdto);
                    allDdtoList.addAll(convertedList);
                } catch (Exception e) {
                    logger.error("CDTO 변환 실패: {} - {}", 
                               cdto != null ? cdto.getClass().getSimpleName() : "null", e.getMessage());
                }
            }
        }
        
        logger.info("=== 다중 CDTO 변환 완료: {}개 → {}개 ===", 
                   cdtos != null ? cdtos.length : 0, allDdtoList.size());
        return allDdtoList;
    }

    /**
     * DDTO 리스트에서 빈 값들을 필터링
     */
    public List<DepositeDDTO> filterValidDdtos(List<DepositeDDTO> ddtoList) {
        if (ddtoList == null) {
            return new ArrayList<>();
        }
        
        List<DepositeDDTO> filteredList = new ArrayList<>();
        
        for (DepositeDDTO ddto : ddtoList) {
            if (isValidDdto(ddto)) {
                filteredList.add(ddto);
            }
        }
        
        logger.info("DDTO 필터링: {}개 → {}개", ddtoList.size(), filteredList.size());
        return filteredList;
    }

    /**
     * 디버그용 - CDTO 객체의 모든 필드 정보를 로그로 출력
     */
    public void debugCdtoFields(Object cdto) {
        if (cdto == null) {
            logger.info("DEBUG: CDTO 객체가 null입니다.");
            return;
        }
        
        Class<?> clazz = cdto.getClass();
        logger.info("=== DEBUG: {} 클래스 필드 분석 ===", clazz.getSimpleName());
        
        // 모든 필드 출력
        Field[] fields = getAllFields(clazz);
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(cdto);
                logger.info("필드: {} (타입: {}) = {}", 
                           field.getName(), field.getType().getSimpleName(), value);
            } catch (Exception e) {
                logger.info("필드 접근 실패: {} - {}", field.getName(), e.getMessage());
            }
        }
        
        // DDTO 런타임 분석 및 매핑 가능성 체크
        DepositeDDTO sampleDdto = new DepositeDDTO();
        String[] ddtoFields = analyzeDdtoFields(sampleDdto);
        
        logger.info("=== DDTO 런타임 분석 및 매핑 가능성 체크 ===");
        for (String ddtoField : ddtoFields) {
            Object value = getCdtoFieldValue(cdto, ddtoField);
            logger.info("DDTO.{} ← CDTO.{} = {}", ddtoField, ddtoField, value);
        }
        
        logger.info("=== DEBUG 완료 ===");
    }

    /**
     * 특정 CDTO 클래스가 DDTO로 변환 가능한지 사전 체크
     */
    public boolean canConvertToDdto(Object cdto) {
        if (cdto == null) {
            return false;
        }
        
        // DDTO를 런타임 분석하여 필드 중 하나라도 매핑 가능하면 변환 가능
        DepositeDDTO sampleDdto = new DepositeDDTO();
        String[] ddtoFields = analyzeDdtoFields(sampleDdto);
        
        for (String fieldName : ddtoFields) {
            Object value = getCdtoFieldValue(cdto, fieldName);
            if (value != null && StringUtils.hasText(value.toString())) {
                return true;
            }
        }
        
        return false;
    }
} 