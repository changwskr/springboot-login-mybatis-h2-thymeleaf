package com.example.login.deposite.business.as;

import com.example.login.deposite.business.dto.DepositeDDTO;
import com.example.login.deposite.transfer.DepositeCDTO;
import com.example.login.deposite.business.dc.DepositeDC;
import com.example.login.deposite.transfer.CommonDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class DepositeAS {

    private DepositeDDTO depositeDDTO;
    private DepositeCDTO depositeCDTO;

    private final DepositeDC depositeDC;

    public DepositeCDTO execute(DepositeCDTO depositeCDTO) {
        CommonDTO commonDTO = depositeCDTO.getComDTO();
        String actionName = commonDTO.getReqName();

        this.depositeCDTO = depositeCDTO;
        this.depositeDDTO = new DepositeDDTO();
        this.depositeDDTO.setCompany(this.depositeCDTO.getCompany());
        this.depositeDDTO.setId(this.depositeCDTO.getId());
        this.depositeDDTO.setName(depositeCDTO.getName());

        List<DepositeDDTO> allList = new ArrayList<DepositeDDTO>();

        switch (actionName == null ? "" : actionName.toLowerCase()) {
            case "put":
                depositeDDTO = depositeDC.restPUT(this.depositeDDTO);
                allList.add(depositeDDTO);
                break;
            case "post":
                depositeDDTO = depositeDC.restPOST(this.depositeDDTO);
                allList.add(depositeDDTO);
                break;
            case "delete":
                int rtn = depositeDC.restDELETE(this.depositeDDTO);
                allList.add(depositeDDTO);
                break;
            case "get":
                depositeDDTO = depositeDC.restGET(this.depositeDDTO);
                allList.add(depositeDDTO);
                break;
            case "getall":
                List<DepositeDDTO> all = depositeDC.restGETALL();
                if (!all.isEmpty()) {
                    depositeDDTO = all.get(0);
                }
                allList = all;
                break;
            default:
                // 알 수 없는 action 처리
                break;
        }

        for(DepositeDDTO io2 : allList){
			System.out.println( "id-" + io2.getId() + "-" + io2.getName());
		}


        depositeCDTO.setDdto(allList);


        return this.depositeCDTO;
    }
} 