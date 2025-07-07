package com.example.login.deposite.business.as;

import com.example.login.deposite.business.dto.DepositeDDTO;
import com.example.login.deposite.transfer.DepositeCDTO;
import com.example.login.deposite.business.dc.DepositeDC;
import com.example.login.deposite.transfer.CommonDTO;
import com.example.login.deposite.web.controller.DepositeController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
@Transactional(transactionManager = "db1TransactionManager")
@RequiredArgsConstructor
public class DepositeAS {

    private DepositeDDTO depositeDDTO;
    private DepositeCDTO depositeCDTO;
    private static final Logger log = LoggerFactory.getLogger(DepositeAS.class);


    private final DepositeDC depositeDC;

    public DepositeCDTO execute(DepositeCDTO depositeCDTO) {
        log.info("[START] DepositeAS.execute - 요청 처리 시작");

        CommonDTO commonDTO = depositeCDTO.getComDTO();
        String actionName = commonDTO.getReqName();
        log.info("요청된 actionName: {}", actionName);

        this.depositeCDTO = depositeCDTO;
        this.depositeDDTO = new DepositeDDTO();
        this.depositeDDTO.setCompany(this.depositeCDTO.getCompany());
        this.depositeDDTO.setId(this.depositeCDTO.getId());
        this.depositeDDTO.setName(depositeCDTO.getName());

        List<DepositeDDTO> allList = new ArrayList<>();

        switch (actionName == null ? "" : actionName.toLowerCase()) {
            case "put":
                log.info("PUT 요청 처리 시작");
                depositeDDTO = depositeDC.restPUT(this.depositeDDTO);
                allList.add(depositeDDTO);
                break;
            case "post":
                log.info("POST 요청 처리 시작");
                depositeDDTO = depositeDC.restPOST(this.depositeDDTO);
                allList.add(depositeDDTO);
                break;
            case "delete":
                log.info("DELETE 요청 처리 시작");
                int rtn = depositeDC.restDELETE(this.depositeDDTO);
                allList.add(depositeDDTO); // 삭제 후에도 반환 정보 저장
                log.info("삭제 처리 결과: {}", rtn);
                break;
            case "get":
                log.info("GET 요청 처리 시작");
                depositeDDTO = depositeDC.restGET(this.depositeDDTO);
                allList.add(depositeDDTO);
                break;
            case "getall":
                log.info("GETALL 요청 처리 시작");
                List<DepositeDDTO> all = depositeDC.restGETALL();
                if (!all.isEmpty()) {
                    depositeDDTO = all.get(0);
                }
                allList = all;
                break;
            default:
                log.warn("알 수 없는 actionName 처리 요청: {}", actionName);
                break;
        }

        for (DepositeDDTO dto : allList) {
            log.info("처리 결과: id={}, name={}", dto.getId(), dto.getName());
        }

        depositeCDTO.setDdto(allList);

        log.info("[END] DepositeAS.execute - 요청 처리 완료");
        return this.depositeCDTO;
    }
}
